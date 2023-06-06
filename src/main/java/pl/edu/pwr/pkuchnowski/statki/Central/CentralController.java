package pl.edu.pwr.pkuchnowski.statki.Central;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import pl.edu.pwr.pkuchnowski.statki.Buoy.Buoy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CentralController {
    @FXML
    public ProgressBar progressBar;
    @FXML
    public Button startButton;

    public class CentralServer {
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        private final int port;

        public CentralServer(int port){

            this.port = port;
        }

        public synchronized void increaseBuoyCounter(){
            buoyCounter++;
        }

        public void start() throws IOException, InterruptedException {
            CentralServerThreadLogic centralServerThreadLogic = new CentralServerThreadLogic();
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            Buoy buoy = new Buoy();
            increaseBuoyCounter();
            buoy.setIndex(port - 58000);
            buoy.setBuoyPosition(buoy);
            System.out.println("Buoy: " + buoy.getIndex() + " X: " + buoy.getPosX() + " Y: " + buoy.getPosY());
            Platform.runLater(() -> {
                welcomeText.setText("Buoys connected: " + buoyCounter);
                double progress = ((double) buoyCounter) /64;
                //System.out.println(progress);
                progressBar.setProgress(progress);
                if(buoyCounter == 64){
                    progressBar.setVisible(false);
                    welcomeText.setText("");
                    drawTheSea();
                }
            });

            System.out.println("Done waiting!" + port);

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine;
            String oldInputLine = "";
            while ((inputLine = in.readLine()) != null) {
                if(inputLine != oldInputLine) {
                    centralServerThreadLogic.analyzeInput(map, buoy, inputLine, buoyCounter);
                    oldInputLine = inputLine;
                }
            }
            System.out.println("good bye");
        }
    }
    @FXML
    private Label welcomeText;
    @FXML
    GridPane map;
    int buoyCounter = 0;

    public void drawTheSea() {
        map.getColumnConstraints().clear();
        map.getRowConstraints().clear();
        map.getChildren().clear();
        ColumnConstraints column = new ColumnConstraints(map.getWidth() / 40);
        column.setHalignment(Pos.CENTER.getHpos());
        RowConstraints row = new RowConstraints(map.getHeight() / 40);
        row.setValignment(Pos.CENTER.getVpos());
        for (int i = 0; i < 40; i++) {
            map.getRowConstraints().add(row);
            map.getColumnConstraints().add(column);
        }
    }
    private int port = 58001;
    List<CentralServer> centralServers = new ArrayList<>();
    @FXML
    protected void onStartButtonClick() {
        startButton.setDisable(true);
        if(buoyCounter == 0) {
            welcomeText.setText("Buoys connected: " + buoyCounter);
            progressBar.setVisible(true);
            for (int i = 0; i < 64; i++) {
                CentralServer server = new CentralServer(port);
                centralServers.add(server);
                Runnable buoyServerRunnable = () -> {
                    try {
                        server.start();
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                };
                Thread buoyServerThread = new Thread(buoyServerRunnable);
                buoyServerThread.start();
                port++;
            }
        }

    }
}