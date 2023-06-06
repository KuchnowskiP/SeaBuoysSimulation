package pl.edu.pwr.pkuchnowski.statki.Central;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

/** @author Piotr Kuchnowski
 *  The CentralApplication class starts JavaFX app for Central
 *  The Central is responsible for receiving data from buoys*/

public class CentralApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CentralApplication.class.getResource("central-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 450, 600);
        stage.setTitle("Central");
        stage.setScene(scene);
        stage.show();
    }
    /**When  CentralApplication starts, it launches the centralPortDealer thread*/
    static CentralPortDealer centralPortDealer = new CentralPortDealer();
    public static void main(String[] args) {
        centralPortDealer.runPortDealerThread();
        launch();
    }
}