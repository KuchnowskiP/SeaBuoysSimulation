package pl.edu.pwr.pkuchnowski.statki.Ship;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ShipController {
    @FXML
    public TextField thirdLastCommand;
    @FXML
    public TextField secondLastCommand;
    @FXML
    public TextField lastCommand;
    public TextFlow textFlow = new TextFlow();
    @FXML
    public Button startButton;


    public class ShipClient {
        public boolean sailing = true;
            public class ShipServer {
                private ServerSocket serverSocket;
                private Socket clientSocket;
                private PrintWriter out;
                private BufferedReader in;
                public void start(int port) throws IOException, InterruptedException {
                    serverSocket = new ServerSocket(port);
                    clientSocket = serverSocket.accept();
                    sailing = false;
                }
            }
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        ShipServer shipServer = new ShipServer();

        public void runServer(int port){
            Runnable shipServerRunnable = () -> {
                try {
                    System.out.println("Server port: " + (port+100));
                    final int serverPort = port + 100;
                    shipServer.start(serverPort);
                } catch (InterruptedException | IOException e) {
                }
            };
            Thread shipServerThread = new Thread(shipServerRunnable);
            shipServerThread.start();
        }
        public void startConnection(String ip, int port) throws IOException, InterruptedException {
            clientSocket = new Socket(ip, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            if(port == 60000) {
                int newPort = Integer.parseInt(in.readLine());
                startConnection("localhost", newPort);
            }
            runServer(port);
            Ship meShip = new Ship();
            meShip.setIndex((port-60000));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(meShip.getIndex());
            boolean firstRun = true;
            while(sailing){
                System.out.println(port);
                String[] divider;;
                if(firstRun){
                    divider = in.readLine().split(";");
                    meShip.setIndex(Integer.parseInt(divider[0]));
                    meShip.setPosX(Integer.parseInt(divider[1]));
                    meShip.setPosY(Integer.parseInt(divider[2]));
                    firstRun = false;

                }else{
                    Random random = new Random();
                    int whatToDo = Math.abs(random.nextInt()%4);
                    if(whatToDo > 0) {
                        String move = meShip.move();
                        out.println(move);
                        divider = in.readLine().split(";");
                        System.out.println(divider[0]);
                        System.out.println(divider[1]);
                        meShip.setPosX(Integer.parseInt(divider[0]));
                        meShip.setPosY(Integer.parseInt(divider[1]));

                        Platform.runLater(() -> {
                            thirdLastCommand.setStyle("-fx-text-fill : grey");
                            thirdLastCommand.setText(secondLastCommand.getText());
                            secondLastCommand.setStyle("-fx-text-fill : grey");
                            secondLastCommand.setText(lastCommand.getText());
                            lastCommand.setText(move);
                        });
                    }else{
                        String scan = meShip.scan();
                        out.println(scan);
                        divider = in.readLine().split("-");
                        Text title = new Text("Last known ships positions\n");
                        Platform.runLater(() -> {
                            textFlow.getChildren().clear();
                            textFlow.getChildren().add(title);
                            for(int i = 0; i < divider.length; i++) {

                                System.out.println(divider[0]);
                                textFlow.getChildren().add(new Text(divider[i]  + "\n"));

                            }
                            thirdLastCommand.setStyle("-fx-text-fill : grey");
                            thirdLastCommand.setText(secondLastCommand.getText());
                            secondLastCommand.setStyle("-fx-text-fill : grey");
                            secondLastCommand.setText(lastCommand.getText());
                            lastCommand.setText(scan);
                        });

                    }
                }
                Platform.runLater(() -> {
                    welcomeText.setText("Current position: " + meShip.getPosX() + ";" + meShip.getPosY());
                });
                Thread.sleep(1000);
            }
            Platform.runLater(() -> {
                welcomeText.setText("Drowned :c");
            });

        }
    }
    @FXML
    Label welcomeText;
    ShipClient client = new ShipClient();
    @FXML
    protected void onStartButtonClick() {
        startButton.setDisable(true);
        welcomeText.setText("Welcome to JavaFX Application!");
        Runnable shipThreadRunnable = () -> {
            try {
                client.startConnection("localhost", 60000);
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        };
        Thread shipThread = new Thread(shipThreadRunnable);
        shipThread.start();
    }
}