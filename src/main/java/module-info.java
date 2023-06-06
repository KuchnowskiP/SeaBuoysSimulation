module pl.edu.pwr.pkuchnowski.statki {
    requires javafx.controls;
    requires javafx.fxml;


    opens  pl.edu.pwr.pkuchnowski.statki.Central to javafx.fxml;
    exports pl.edu.pwr.pkuchnowski.statki.Central;
    exports pl.edu.pwr.pkuchnowski.statki.Buoy;
    exports pl.edu.pwr.pkuchnowski.statki.World;
    exports  pl.edu.pwr.pkuchnowski.statki.Ship;
    opens pl.edu.pwr.pkuchnowski.statki.Ship to javafx.fxml;
    opens pl.edu.pwr.pkuchnowski.statki.World to javafx.fxml;
}