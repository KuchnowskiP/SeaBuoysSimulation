package pl.edu.pwr.pkuchnowski.statki.World;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import pl.edu.pwr.pkuchnowski.statki.Ship.Ship;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/** @author Piotr Kuchnowski
 * The WorldController class provides drawing the World Map in the World JavaFX app.
 * It also provides servers for shipsClients to connect to. It reads ships positions and draw them on the map.*/

public class WorldController {
    public List<Ship> shipsAtTheSea = new ArrayList<>();
    public Button startButton;

    /**@author Piotr Kuchnowski
     * The WorldServer class provides servers for ships. It receives their coordinates which are used for drawing them on the map */
    public class WorldServer {
        private ServerSocket serverSocket;
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        WorldServerShipHandling shipHandling = new WorldServerShipHandling();

        public void runWaitingForShipThread(){
            Thread worldServerThread = new Thread(worldThreadRunnable);
            worldServerThread.setName(String.valueOf(nextServerPort));
            worldServerThread.start();
            nextServerPort++;
        }
        public void start(int port) throws IOException {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            runWaitingForShipThread();
            Platform.runLater(() -> {
                welcomeText.setText("I SEE SHIIIIP :)");
            });
            Ship ourShip;
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            ourShip = shipHandling.generateShip(out, shipsAtTheSea, map);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ourShip.setIndex(Integer.parseInt(in.readLine()));
            shipsAtTheSea.add(ourShip);
            System.out.println(ourShip.getIndex());
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                String[] divider;
                divider = inputLine.split(";");
                System.out.println(divider[0] + ";" + divider[1]);
                if(Objects.equals(divider[0].toLowerCase(), "move")){
                    if(shipHandling.tryToMoveTheShip(shipsAtTheSea, map, divider[1],ourShip)){
                        out.println(ourShip.getPosX()+";"+ourShip.getPosY());
                    }else{
                        out.println(ourShip.getPosX()+";"+ourShip.getPosY());
                    }
                }
                if(Objects.equals(divider[0].toLowerCase(), "scan")){
                    StringBuilder positions = new StringBuilder();
                    for(Ship ship : shipsAtTheSea){
                        positions.append(ship.getPosX()+";"+ship.getPosY()+"-");
                    }
                    out.println(positions);
                }
                if (".".equals(inputLine)) {
                    out.println("good bye");
                    break;
                }
                shipHandling.checkCollisions(shipsAtTheSea, ourShip, map);
            }
            System.out.println("exited");
        }
    }
    @FXML
    private Label welcomeText;
    @FXML
    GridPane map;

    /** drawTheSea method draws empty sea, without ships, after the WorldApplication start button being pressed*/
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
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                Region r = new Region();
                if(j % 5 == 0){
                    if(i % 5 == 0){
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black grey grey black; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }else if(i % 5 == 4) {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey black black; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }else{
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey grey black; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }
                }else if(j % 5 == 4){
                    if(i % 5 == 0){
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black black grey grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }else if(i % 5 == 4) {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey black black grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }else {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey black grey grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }
                }else if(i % 5 == 0){
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black grey grey grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                }else if(i % 5 == 4){
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey black grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                }
                else{
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                }
                map.add(r, j, i);
            }
        }
    }
    List<WorldServer> worldServersList = new ArrayList<>();

    Runnable worldThreadRunnable = () -> {
        WorldServer server = new WorldServer();
        worldServersList.add(server);
        try {
            server.start(Integer.parseInt(Thread.currentThread().getName()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    int nextServerPort = 60001;
    @FXML
    protected void onStartButtonClick() {
        startButton.setDisable(true);
        Thread worldServerThread = new Thread(worldThreadRunnable);
        worldServerThread.setName(String.valueOf(nextServerPort));
        worldServerThread.start();
        nextServerPort++;

        drawTheSea();
    }
}