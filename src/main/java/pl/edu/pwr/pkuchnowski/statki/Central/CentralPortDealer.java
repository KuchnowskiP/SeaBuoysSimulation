package pl.edu.pwr.pkuchnowski.statki.Central;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** @author Piotr Kuchnowski
 *  The CentralPortDealer class provides the allocation of ports to successively switched on buoys. Allocates exactly 64 ports.
 *  The port allocation thread is called from CentralApplication class when it starts*/

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
            out.println(passedPort);
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
