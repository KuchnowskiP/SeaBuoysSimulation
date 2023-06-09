package pl.edu.pwr.pkuchnowski.statki.Buoy;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/** @author Piotr Kuchnowski
 *  The BuoyApplication class starts JavaFX app for one buoy instance
 *  The Buoy receives data about ships from the World, calculates sea levels and passing them to the Central*/

public class BuoyApplication {
    private static BuoyClient client = new BuoyClient();

    public static void main(String[] args){

        Runnable buoyThreadRunnable = () -> {
            try {
                //Thread.sleep(100);
                client.startConnection("localhost", 58000);
            } catch (IOException e) {
                try {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    Files.writeString(Path.of("error.txt"),sw.toString());
                } catch (IOException ex) {
                }
            }
        };
        Thread buoyThread = new Thread(buoyThreadRunnable);
        buoyThread.start();
    }
}


