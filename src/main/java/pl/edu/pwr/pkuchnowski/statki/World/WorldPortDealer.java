package pl.edu.pwr.pkuchnowski.statki.World;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** @author Piotr Kuchnowski
 *  The WorldPortDealer class provides the allocation of ports to successively connecting ships. It allocates as many ports as requests it receives.
 *  The port allocation thread is called from WorldApplication class when it starts*/

public class WorldPortDealer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private List<Integer> occupiedPorts = new ArrayList<>();

    public void start(int port) throws IOException {
        occupiedPorts.add(port);
        int passedPort;
        serverSocket = new ServerSocket(port);
        while(true) {
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            passedPort = occupiedPorts.stream().max(Comparator.comparing((Integer::valueOf))).get();
            passedPort++;
            occupiedPorts.add(passedPort);
            out.println(passedPort);
            if(occupiedPorts.isEmpty()){
                break;
            }
        }

        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            if (".".equals(inputLine)) {
                out.println("good bye");
                break;
            }
            out.println(inputLine);
        }

    }
}
