package pl.edu.pwr.pkuchnowski.statki.Buoy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/** @author Piotr Kuchnowski
 *  The BuoyClient class provides connecting to the CentralServer
 *  It calculates sea level in a loop and sends the results to the CentralServer*/

public class BuoyClient {
    List<int[][]> sectorsList = new ArrayList<>();

    /** @author Piotr Kuchnowski
     *  The BuoyServer class provides ServerSocket for WorldBuoyClient to connect to when there are ships in sight of the buoy.
     *  This class also calculates the sea level for a passing ship*/
    public class BuoyServer {
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;
        int[][] sector = new int[5][5]; //representation of one 5x5 map sector being watched by the buoy
        /**Method calculateSeaLevel, knowing that the ship knows its position and the position of the buoy is known,
         * calculates the location of the wave produced by the ship and adds the sector of the buoy to the list of registered waves*/
        public void calculateSeaLevel(String input, Buoy buoy){
            fillTheSector();
            String[] divider;
            divider = input.split(";");
            int shipPosX = Integer.parseInt(divider[0]);
            int shipPosY = Integer.parseInt(divider[1]);
            int differenceX = shipPosX - buoy.getPosX();
            int differenceY = shipPosY - buoy.getPosY();
            if (differenceY != 0) {
                for (int i = 0; i < Math.abs(differenceY); i++) {
                    if (differenceY < 0) {
                        for(int j = 0; j < 5; j++){
                            if(j == 4){
                                sector[j] = new int[]{0,0,0,0,0};
                            }else
                                sector[j] = sector[j + 1];
                        }
                    } else {
                        for(int j = 4; j >= 0; j--){
                            if(j == 0) {
                                sector[j] = new int[]{0,0,0,0,0};
                            }else {
                                sector[j] = sector[j - 1];
                            }
                        }
                    }
                }
            }
            if (differenceX != 0) {
                for (int i = 0; i < Math.abs(differenceX); i++) {
                    if (differenceX < 0) {
                        for(int j = 0; j < 5; j++){
                            sector[j][0] = sector[j][1];
                            sector[j][1] = sector[j][2];
                            sector[j][2] = sector[j][3];
                            sector[j][3] = sector[j][4];
                            sector[j][4] = 0;
                        }
                    } else {
                        for(int j = 0; j < 5; j++){
                            sector[j][4] = sector[j][3];
                            sector[j][3] = sector[j][2];
                            sector[j][2] = sector[j][1];
                            sector[j][1] = sector[j][0];
                            sector[j][0] = 0;
                        }
                    }
                }
            }
           synchronized (sectorsList) {
                sectorsList.add(sector);
           }
            sector = new int[5][5];
        }
        public void fillTheSector(){
            int[] sectorArray = new int[]{0, 1, 2, 1, 0, 1, 2, 3, 2, 1, 2, 3, 4, 3, 2, 1, 2, 3, 2, 1, 0, 1, 2, 1, 0};
            int arrayCounter = 0;
            for(int i = 0; i < 5; i++){
                for(int j = 0; j < 5; j++){
                    sector[i][j] = sectorArray[arrayCounter];
                    arrayCounter++;
                }
            }
        }
        public void start(int port) throws IOException, InterruptedException {
            serverSocket = new ServerSocket(port);
            Buoy buoy = new Buoy();
            buoy.setIndex(port-58100);
            buoy.setBuoyPosition(buoy);
            while(true) {
                clientSocket = serverSocket.accept();
                System.out.println("World connected to buoy: " + (port - 58100));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                String inputLine = in.readLine();
                System.out.println(inputLine);
                out.println("I'am buoy number: " + buoy.getIndex() + " and I'am here: " + buoy.getPosX() + ";" + buoy.getPosY());
                calculateSeaLevel(inputLine, buoy);

            }
        }
    }
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    StringBuilder seaLevel = new StringBuilder();
    BuoyServer buoyServer = new BuoyServer();
    /**runServer method opens server for ships passing by*/
    public void runServer(int port){
        Runnable buoyServerRunnable = () -> {
            try {
                System.out.println("Server port: " + (port+100));
                final int serverPort = port + 100;
                buoyServer.start(serverPort);
            } catch (InterruptedException | IOException e) {
            }
        };
        Thread buoyServerThread = new Thread(buoyServerRunnable);
        buoyServerThread.start();
    }

    /**Method sumWaves, calculates the sum of waves when at least two ships passed close enough to each other*/
    public int[][] sumWaves(){
        synchronized (sectorsList) {
            int[][] result = new int[5][5];
            if(sectorsList.size() != 1) {
                for (int[][] sectorToAdd : sectorsList) {
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 5; j++) {
                            result[i][j] += sectorToAdd[i][j];
                            System.out.print(result[i][j]);
                        }
                        System.out.println();
                    }
                }
            }else{
                result = sectorsList.get(0);
            }
            sectorsList.clear();
            return result;
        }
    }
    /**startConnection connects buoy to the CentralServer, then it waits for its new port
     * It also repetitively sends sea levels from its sector to the Central*/
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        if(port == 58000) {
            int newPort = Integer.parseInt(in.readLine());
            startConnection("localhost", newPort);
        }
        runServer(port); //opens server for passing ships
        out = new PrintWriter(clientSocket.getOutputStream(), true);

        while(true){

            int[][] sumOfWaves = sumWaves();
            seaLevel = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    seaLevel.append(sumOfWaves[i][j]);
                }
            }
            out.println(seaLevel);
            seaLevel = new StringBuilder();
            for (int i = 0; i < 25; i++) {
                seaLevel.append("0");
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }
}
