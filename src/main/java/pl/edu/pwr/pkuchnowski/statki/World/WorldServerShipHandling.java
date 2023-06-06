package pl.edu.pwr.pkuchnowski.statki.World;

import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import pl.edu.pwr.pkuchnowski.statki.Ship.Ship;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class WorldServerShipHandling {
    public String getRandomUnoccupiedField(List<Ship> shipsAtTheSea) {
        Random random = new Random();
        boolean hasBeenFound = false;
        int randomizedXPos = 0;
        int randomizedYPos = 0;
        searching:
        while (!hasBeenFound) {
            randomizedXPos = Math.abs(random.nextInt() % 40);
            randomizedYPos = Math.abs(random.nextInt() % 40);
            synchronized (shipsAtTheSea) {
                for (Ship ship : shipsAtTheSea) {
                    if (ship.getPosX() == randomizedXPos && ship.getPosY() == randomizedYPos) {
                        continue searching;
                    }
                }
            }
            hasBeenFound = true;
        }
        return randomizedXPos + ";" + randomizedYPos;
    }

    public synchronized Ship generateShip(PrintWriter out, List<Ship> shipsAtTheSea, GridPane map){
        Ship generatedShip = new Ship();
        String[] divider;
        String generatedPosition = getRandomUnoccupiedField(shipsAtTheSea);
        //generatedPosition = "38;38";
        divider = generatedPosition.split(";");
        int posX = Integer.parseInt(divider[0]);
        int posY = Integer.parseInt(divider[1]);
        generatedShip.setIndex(shipsAtTheSea.size()+1);
        generatedShip.setPosX(posX);
        generatedShip.setPosY(posY);
        out.println(generatedShip.getIndex()+";"+generatedPosition);

        WorldBuoyClient worldBuoyClient = new WorldBuoyClient(posX,posY);
        Thread informant = new Thread(() -> {
            worldBuoyClient.informThaBuoys();
        });
        informant.start();
        //shipsAtTheSea.add(generatedShip);
        Platform.runLater(() -> {
            Rectangle shipRect = new Rectangle(map.getWidth()/40 - 5, map.getWidth()/40 - 5);
            map.add(shipRect,posX,posY);
        });

        return generatedShip;
    }
    public void checkCollisions(List<Ship> shipsAtTheSea, Ship ourShip, GridPane map) throws IOException {
        for (Ship ship : shipsAtTheSea) {
            if (ship.getIndex() != ourShip.getIndex()) {
                if (ship.getPosX() == ourShip.getPosX() && ship.getPosY() == ourShip.getPosY()) {
                    Socket clientSocketToShip1 = new Socket("localhost", (60100 + ship.getIndex()));
                    Socket clientSocketToShip2 = new Socket("localhost", (60100 + ourShip.getIndex()));
                    shipsAtTheSea.removeIf(shipToRemove -> ship.getIndex() == shipToRemove.getIndex() || ourShip.getIndex() == shipToRemove.getIndex());
                    int oldPosX = ship.getPosX();
                    int oldPosY = ship.getPosY();
                    Region r = new Region();
                    if (oldPosX % 5 == 0) {
                        if (oldPosY % 5 == 0) {
                            r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black grey grey black; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                        } else if (oldPosY % 5 == 4) {
                            r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey black black; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                        } else {
                            r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey grey black; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                        }
                    } else if (oldPosX % 5 == 4) {
                        if (oldPosY % 5 == 0) {
                            r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black black grey grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                        } else if (oldPosY % 5 == 4) {
                            r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey black black grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                        } else {
                            r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey black grey grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                        }
                    } else if (oldPosY % 5 == 0) {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black grey grey grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    } else if (oldPosY % 5 == 4) {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey black grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    } else {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }
                    synchronized (map) {
                            Platform.runLater(() -> {
                                map.getChildren().removeIf(node -> GridPane.getRowIndex(node) == oldPosY && GridPane.getColumnIndex(node) == oldPosX);
                                map.add(r, oldPosX, oldPosY);
                            });
                    }
                }
            }
        }
    }
    public void moveTheShip(GridPane map, Ship ship, int checkingPosY, int checkingPosX) {
        WorldBuoyClient worldBuoyClient = new WorldBuoyClient(checkingPosX,checkingPosY);
        System.out.println("Moving ship from: " + ship.getPosX() +";" + ship.getPosY());
        int oldPosX = ship.getPosX();
        int oldPosY = ship.getPosY();
        ship.setPosX(checkingPosX);
        ship.setPosY(checkingPosY);
        synchronized (map) {
            Platform.runLater(() -> {
                Region r = new Region();
                if(oldPosX % 5 == 0){
                    if(oldPosY % 5 == 0){
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black grey grey black; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }else if(oldPosY % 5 == 4) {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey black black; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }else{
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey grey black; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }
                }else if(oldPosX % 5 == 4){
                    if(oldPosY % 5 == 0){
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black black grey grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }else if(oldPosY % 5 == 4) {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey black black grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }else {
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey black grey grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                    }
                }else if(oldPosY % 5 == 0){
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black grey grey grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                }else if(oldPosY % 5 == 4){
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey black grey; -fx-min-width: " + map.getHeight() / 40 + "; -fx-min-height:" + map.getHeight() / 40 + "; -fx-max-width:" + map.getHeight() / 40 + "; -fx-max-height: " + map.getHeight() / 40 + ";");
                }
                else{
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                }
                map.getChildren().removeIf(node -> GridPane.getRowIndex(node) == oldPosY && GridPane.getColumnIndex(node) == oldPosX);
                map.add(r, oldPosX, oldPosY);
                Rectangle shipRect = new Rectangle(map.getWidth() / 40 - 5, map.getWidth() / 40 - 5);
                map.add(shipRect, ship.getPosX(), ship.getPosY());
            });
        }
        System.out.println("to: " + ship.getPosX() +";" + ship.getPosY());
        Thread informant = new Thread(() -> {
            worldBuoyClient.informThaBuoys();
        });
        informant.start();
    }
    public synchronized boolean tryToMoveTheShip(List<Ship> shipsAtTheSea, GridPane map, String direction, Ship ship){
        int checkingPosY = ship.getPosY();
        int checkingPosX = ship.getPosX();
        System.out.println(direction);
        if(direction.equals("0")){
            checkingPosY -= 1;
            if(ship.getPosY() == 0){
                return false;
            }
            moveTheShip(map, ship, checkingPosY, checkingPosX);
        }if(direction.equals("1")){
            checkingPosY -= 1;
            checkingPosX += 1;
            if(ship.getPosX() == 39 || ship.getPosY() == 0){
                return false;
            }
            moveTheShip(map, ship, checkingPosY, checkingPosX);
        }if(direction.equals("2")){
            checkingPosX += 1;
            if(ship.getPosX() == 39){
                return false;
            }
            moveTheShip(map, ship, checkingPosY, checkingPosX);
        }if(direction.equals("3")){
            checkingPosY += 1;
            checkingPosX += 1;
            if(ship.getPosY() == 39 || ship.getPosX() == 39){
                return false;
            }
            moveTheShip(map, ship, checkingPosY, checkingPosX);
        }if(direction.equals("4")){
            checkingPosY += 1;
            if(ship.getPosY() == 39){
                return false;
            }
            moveTheShip(map, ship, checkingPosY, checkingPosX);
        }if(direction.equals("5")){
            checkingPosY += 1;
            checkingPosX -= 1;
            if(ship.getPosX() == 0 || ship.getPosY() == 39){
                return false;
            }
            moveTheShip(map, ship, checkingPosY, checkingPosX);
        }if(direction.equals("6")){
            checkingPosX -= 1;
            if(ship.getPosX() == 0){
                return false;
            }
            moveTheShip(map, ship, checkingPosY, checkingPosX);
        }if(direction.equals("7")){
            checkingPosY -= 1;
            checkingPosX -= 1;
            if(ship.getPosX() == 0 || ship.getPosY() == 0){
                return false;
            }
            moveTheShip(map, ship, checkingPosY, checkingPosX);
        }
        return true;
    }


}
