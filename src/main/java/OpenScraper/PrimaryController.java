package OpenScraper;

import java.awt.*;
import java.io.File;

import Engine.Downloader;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

import Engine.DownloaderEngine;
import Engine.FileImporter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
public class PrimaryController {
    @FXML
    Label line_number;
    @FXML
    ProgressBar pb;
    @FXML
    ListView lv;
    @FXML
    Label info_label;
    Thread downloader_thread;
    /**
     * Load The file Containing the Set
     * of Links to be scraped as Images
     */
    @FXML
    public void updateLineNumber()
    {
        line_number.setText("Line :"+lv.getFocusModel().getFocusedIndex());
    }
    @FXML
    public void OpenFile()throws Exception
    {   //delete last Temporary File


        //Create A Temporary File

        //Open A File Chooser To Select Files
        FileChooser fc=new FileChooser();
        List<File> list_of_files=fc.showOpenMultipleDialog(lv.getScene().getWindow());
        Iterator file_iterator=list_of_files.iterator();
        String paths="";
        while(file_iterator.hasNext())
        {
           paths+=file_iterator.next()+"\r\n";
        }


        FileImporter fi=new FileImporter(paths);
        pb.progressProperty().bind(fi.progressProperty());
        info_label.textProperty().bind(fi.messageProperty());

        Thread file_worker=new Thread(fi);

        file_worker.setDaemon(true);
        file_worker.start();



    }

    /**
     * Start The Process Of Downloading Stuff From THe Links
     */
    boolean isRunning=false;
    @FXML
    public void start()
    {
        if(new File("temp.links").exists()) {
            Downloader de = new Downloader(lv, "temp.links");
            downloader_thread = new Thread(de);
            downloader_thread.setDaemon(true);
            pb.progressProperty().bind(de.progressProperty());
            info_label.textProperty().bind(de.messageProperty());
            if (!isRunning) {
                System.out.println("here");
                downloader_thread.start();
                isRunning = true;

            } else {
                downloader_thread.stop();
                isRunning = false;
            }
        }
        else
        {
            //Alert, Import Links First

        }
    }
    boolean isPaused=false;
    @FXML
    public void pause() {
        if(!isPaused) {
            downloader_thread.suspend();
            isPaused=true;
        }
            else {
            downloader_thread.resume();
            isPaused=false;
        }
    }
    @FXML
    private void Open_Settings() throws IOException {
        FXMLLoader fxmlloader=new FXMLLoader(getClass().getResource("secondary.fxml"));
        Parent root1=(Parent)fxmlloader.load();
        Stage stage=new Stage();

        stage.setTitle("Settings Dialog");
        stage.setScene(new Scene(root1));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
        stage.setResizable(false);
        //stage.getIcons().add(new Image("Images/qis.png"));

    }

    /**
     * Author section
     */
    @FXML
    private void about_author()
    {

        Alert al=new Alert(Alert.AlertType.INFORMATION, "",ButtonType.OK);
        al.setHeaderText("About The Author");
        al.setContentText("Made By Akshat Mishra");

        al.showAndWait();
    }
    /**
     * Takes The User To The github Page
     */
    @FXML
    private void documentation()throws Exception
    {

        Desktop.getDesktop().browse(URI.create("https://github.com/Akshat-Mishra101/QuickImageScraper"));
    }
}
