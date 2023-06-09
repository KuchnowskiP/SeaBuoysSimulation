package pl.edu.pwr.pkuchnowski.statki.Central;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import pl.edu.pwr.pkuchnowski.statki.Buoy.Buoy;

/** @author Piotr Kuchnowski
 *  The CentralServerThreadLogic class provides operating logic - methods for the server receiving the buoy*/

public class CentralServerThreadLogic {
    public void analyzeInput(GridPane map, Buoy buoy, String inputLine, int buoyCounter){
        if (inputLine.length() == 25 && buoyCounter == 64) {
            Label[] seaLevel = new Label[25];
            char[] inputCharArray = inputLine.toCharArray();
            for (int i = 0; i < 25; i++) {
                seaLevel[i] = new Label(String.valueOf(inputCharArray[i]));
            }
            buoy.setBuoyPosition(buoy);

            System.out.println(buoy.getIndex() +": "+ inputLine);

            int finalI = buoy.getPosY() - 2;

            int finalJ = buoy.getPosX() - 2;

            final int[] labelCounter = {0};
            for (int k = 0; k < 5; k++) {
                for (int l = 0; l < 5; l++) {
                    int finalK = k;
                    int finalL = l;
                    Platform.runLater(() -> {
                    synchronized (map) {
                        map.getChildren().removeIf(node -> GridPane.getColumnIndex(node) == finalJ + finalL && GridPane.getRowIndex(node) == finalI + finalK);
                        map.add(seaLevel[labelCounter[0]], finalJ + finalL, finalI + finalK);
                        labelCounter[0]++;
                    }
                    });
                }
            }
            Platform.runLater(() -> recreateSector(finalJ, finalI, map));
        }
    }
    public synchronized void recreateSector(int j, int i, GridPane map){
        for(int k = 0; k < 5; k++){
            for(int l = 0; l < 5; l++){
                Region r = new Region();
                if(k == 0){
                    if(l == 0){
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black grey grey black; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }else if(l ==4){
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black black grey grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }else{
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: black grey grey grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }
                }else if(k == 4){
                    if(l == 0){
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey black black; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }else if(l == 4){
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey black black grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");

                    }else{
                        r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey black grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                    }
                }else if(l == 0){
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey grey grey black; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                }else if(l == 4){
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey black grey grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                }else {
                    r.setStyle("-fx-background-color: transparent; -fx-border-style: solid; -fx-border-width: 1; -fx-border-color: grey; -fx-min-width: "+ map.getHeight()/40 +"; -fx-min-height:"+ map.getHeight()/40 +"; -fx-max-width:"+ map.getHeight()/40 +"; -fx-max-height: "+ map.getHeight()/40 +";");
                }
                map.add(r, j+l, i+k);
            }
        }
    }
}
