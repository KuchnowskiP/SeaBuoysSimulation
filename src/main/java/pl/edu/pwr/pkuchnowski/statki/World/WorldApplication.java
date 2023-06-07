package pl.edu.pwr.pkuchnowski.statki.World;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/** @author Piotr Kuchnowski
 *  The WorldApplication class starts JavaFX app for World
 *  The World is responsible for receiving data from ships, and sending data to buoys*/

public class WorldApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(WorldApplication.class.getResource("world-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 600);
        stage.setTitle("World!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        WorldPortDealer worldPortDealer = new WorldPortDealer();
        Runnable portDealing = () -> {
            try {
                worldPortDealer.start(60000);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Thread portDealerThread = new Thread(portDealing);
        portDealerThread.start();
        launch();
    }
}