package OpenScraper;

import java.awt.*;
import java.io.File;

import Engine.Downloader;
import Engine.Properties;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.notifications.NotificationPos;
import io.github.palexdev.materialfx.notifications.NotificationsManager;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import Engine.DownloaderEngine;
import Engine.FileImporter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

public class PrimaryController implements Initializable {
    @FXML
    MFXToggleButton pause_button;
    @FXML
    MFXButton start_button;
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
     * 
     */
    @FXML
    public void updateLineNumber()
    {
        line_number.setText("Line :"+lv.getFocusModel().getFocusedIndex());
    }
    @FXML
    public void stop_thread()
    {
    if(downloader_thread!=null)
    {    downloader_thread.stop();
         start_button.setDisable(false);
         info_label.setText("Task Cancelled");
    }
    }
    
    
    
    @FXML
    public void OpenFile()throws Exception
    {   //delete last Temporary File


        //Create A Temporary File

        //Open A File Chooser To Select Files
        FileChooser fc=new FileChooser();
        List<File> list_of_files=fc.showOpenMultipleDialog(lv.getScene().getWindow());
        if(list_of_files!=null){
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
        


    }

    /**
     * Start The Process Of Downloading Stuff From THe Links
     */

    @FXML
    public void start()
    {
        lv.getItems().removeAll();
        System.out.println("Inside The Function");
        if(new File("temp.links").exists()) {
            System.out.println("Inside The if statement");
            Downloader de = new Downloader(lv, "temp.links");
            downloader_thread = new Thread(de);
            downloader_thread.setDaemon(true);
            pb.progressProperty().bind(de.progressProperty());
            info_label.textProperty().bind(de.messageProperty());
            if (!downloader_thread.isAlive()) {
                System.out.println("here");
                start_button.setDisable(true);
                downloader_thread.start();

                pause_button.setDisable(false);
                System.out.println("is not running and we started it");


            } else {

                downloader_thread.stop();
                System.out.println("It is running and we stopped it");

            }
            de.setOnSucceeded(e->{
              start_button.setDisable(false);
              pause_button.setDisable(true);
              shownotif();
             
            });
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
        stage.getIcons().add(new Image(Properties.path_to_icon));

    }

    /**
     * Author section
     */
    private void shownotif()
    {
        NotificationPos pos = NotificationPos.BOTTOM_RIGHT;
        showNotification(pos);
    }
    @FXML
    private void about_author()
    {

        Alert al=new Alert(Alert.AlertType.INFORMATION, "",ButtonType.OK);
        al.setHeaderText("About The Author");
        al.setContentText("Made By Akshat Mishra");
        Stage stage = (Stage) al.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("Images/qis.png")); // To add an icon
        al.showAndWait();
    }
    /**
     * Takes The User To The github Page
     */
    @FXML
    private void documentation()throws Exception
    {

        Desktop.getDesktop().browse(URI.create("https://github.com/Akshat-Mishra101/QuickImageScraper#readme"));
    }

    private MFXNotification buildNotification() {
        Region template = getRandomTemplate();
        MFXNotification notification = new MFXNotification(template, true, true);
        notification.setHideAfterDuration(Duration.seconds(3));

        if (template instanceof SimpleMFXNotificationPane) {
            SimpleMFXNotificationPane pane = (SimpleMFXNotificationPane) template;
            pane.setCloseHandler(closeEvent -> notification.hideNotification());
        } else {
            MFXDialog dialog = (MFXDialog) template;
            dialog.setCloseHandler(closeEvent -> notification.hideNotification());
        }

        return notification;
    }

    private Region getRandomTemplate() {
        FontIcon icon1 = new FontIcon();
        icon1.setIconLiteral("fas-cocktail");
        icon1.setIconColor(Color.AQUAMARINE);
        icon1.setIconSize(15);
        return new SimpleMFXNotificationPane(
                icon1,
                "Quick Image Scraper",
                "Alert!",
                "Image Scraping Is Complete"
        );
    }
    private void showNotification(NotificationPos pos) {
        MFXNotification notification = buildNotification();
        NotificationsManager.send(pos, notification);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pause_button.setDisable(true);
    }
}
