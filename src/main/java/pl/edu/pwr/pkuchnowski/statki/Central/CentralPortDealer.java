package pl.edu.pwr.pkuchnowski.statki.Central;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CentralPortDealer {
    List<Integer> occupiedPorts = new ArrayList<>();
    public void start(int port) throws IOException, InterruptedException {
        occupiedPorts.add(port);
        int passedPort;
        ServerSocket serverSocket = new ServerSocket(port);
        Socket clientSocket;
        while (true) {
            clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            passedPort = occupiedPorts.stream().max(Comparator.comparing((Integer::valueOf))).get();
            passedPort++;
            if (passedPort > 58064){
                break;
            }
            occupiedPorts.add(passedPort);
            //System.out.println("Passin port " + passedPort);
            out.println(passedPort);
            //Thread.sleep(100);
        }

    }
    public void runPortDealerThread(){
        Runnable portDealing = () -> {
            try {
                start(58000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread portDealerThread = new Thread(portDealing);
        portDealerThread.start();
    }
}
