module OpenScraper {
    //The Required Modules For Building The Scraper
    requires javafx.controls;
    requires javafx.fxml;
    requires org.jsoup;
    requires MaterialFX;
    requires java.desktop;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;

    requires org.apache.commons.lang3;
    opens OpenScraper to javafx.fxml;
    exports OpenScraper;
}