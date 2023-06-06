/** @author Piotr Kuchnowski
 *  The ShipApplication class starts JavaFX app for one ship instance*/

package pl.edu.pwr.pkuchnowski.statki.Ship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;



import java.io.IOException;
public class ShipApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ShipApplication.class.getResource("ship-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 310, 200);
        stage.setTitle("Ship");
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch();
    }
}