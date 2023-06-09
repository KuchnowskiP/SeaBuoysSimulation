package pl.edu.pwr.pkuchnowski.statki.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/** @author Piotr Kuchnowski
 * The WorldBuoyClient class provides client to inform buoys about passing ships nearby*/

public class WorldBuoyClient {
    int posX;
    int posY;
    public WorldBuoyClient(int posX, int posY){
        this.posX = posX;
        this.posY = posY; //coordinates of the calling ship
    }
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws IOException, InterruptedException {
        clientSocket = new Socket(ip, port);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        System.out.println("Sending info to buoy number: " + (port - 58100));
        out.println(posX+";"+posY);
        System.out.println(in.readLine());
        clientSocket.close();
    }

    public void informer(int port){
        try {
            startConnection("localhost", port);
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**informBuoys method basing on ship positions, calculates which buoys should receive information
     * about passing ship. Then it sends ship's coordinates to calculated buoys through informer method*/
    public void informBuoys(){
        int port;
        int posYMinus2 = posY - 2;
        int posXMinus2 = posX - 2;
        if(posXMinus2 < 0){
            posXMinus2 = 0;
        }
        if(posYMinus2 < 0){
            posYMinus2 = 0;
        }
        port = 8 * (posYMinus2 / 5) + (posXMinus2 / 5) + 1;
        port += 58100;

        if(port >= 58157 && port < 58164){
            informer(port);
            informer((port+1));
        }else if(port == 58164){
            informer(port);
        }else if((port - 58100) % 8 == 0){
            informer(port);
            informer((port+8));
        }else{
            informer(port);
            informer((port+1));
            informer((port+8));
            informer((port+9));
        }
    }
}
