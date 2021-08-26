package OpenScraper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Engine.Properties;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

public class SecondaryController implements Initializable {
    @FXML
    Spinner scraper_retries;
    @FXML
    Spinner downloader_retries;
    @FXML
    TextField proxy;
    @FXML
    MFXCheckbox url_encoding;
    @FXML
    MFXToggleButton Threading;
    @FXML
    MFXCheckbox append_random_strings;
    @FXML
    MFXCheckbox save_images_in_different_folders;
    @FXML
    MFXCheckbox save_image_data;
    @FXML
    MFXComboBox total_threads;
    @FXML
    Spinner scraping_timer;
    @FXML
    Spinner Downloading_timer;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Properties.load();//Load The Properties Onto The Program

            ObservableList<String> ol= FXCollections.observableArrayList();
            ol.add("2 Threads");
            ol.add("N Threads");
            total_threads.setItems(ol);
           total_threads.setDisable(true);
           load_properties_on_the_gui();

        }
        catch(Exception e){}
        }

    /**
     * This Function loads
     * the properties on the GUI Form
     *
     */


        public void load_properties_on_the_gui()
        {

            proxy.setText(Properties.get("proxy"));
            url_encoding.setSelected(Properties.get("encode").equals("YES")?true:false);
            scraping_timer.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000,10000,Integer.parseInt(Properties.get("timeout"))));
            Downloading_timer.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1000,10000,Integer.parseInt(Properties.get("dtimeout"))));
            Threading.setSelected(Properties.get("threads").equals("NO")?false:true);

               System.out.println("Threading ids"+Threading.isSelected());
             total_threads.setDisable(!Threading.isSelected());


             if(Threading.isSelected()) {
                 Threading.setText("Enabled");
                 String result=(Properties.get("threads").equals("2")?"2 Threads":"N Threads");
                 if(result.equals("2 Threads"))
                 total_threads.getSelectionModel().selectFirst();
                 else
                     total_threads.getSelectionModel().selectLast();
             }

            append_random_strings.setSelected(Properties.get("names").equals("YES")?true:false);
            save_image_data.setSelected(Properties.get("save").equals("YES")?true:false);
            save_images_in_different_folders.setSelected(Properties.get("image_saving").equals("YES")?true:false);
            scraper_retries.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,Integer.parseInt(Properties.get("sretry"))));
            downloader_retries.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,10000,Integer.parseInt(Properties.get("dretry"))));
        }
        @FXML
        public void reset_defaults()throws Exception
        {
            String DefaultkeysAndValues[][]={{"proxy","encode","timeout","dtimeout","threads","names","save","image_saving","sretry","dretry"},{"","NO","3000","3000","NO","NO","NO","NO","4","4"}};
            for(int i=0;i<10;i++)
                Properties.update(DefaultkeysAndValues[0][i],DefaultkeysAndValues[1][i]);
            Properties.Save(); //Save The Properties In The File
            load_properties_on_the_gui(); //Load Them On Your GUI
        }

    /**
     * This function updates the Save File
     */
        @FXML
        public void apply_properties()throws Exception
        {    String thread_val="";
            if(Threading.isSelected())
                 thread_val=total_threads.getSelectionModel().getSelectedItem().toString().equals("2 Threads")?"2":"N";
            else
                thread_val="NO";

            String UpdatedKeysAndValues[][]={{"proxy","encode","timeout","dtimeout","threads","names","save","image_saving","sretry","dretry"},{proxy.getText(),url_encoding.isSelected()?"YES":"NO",scraping_timer.getValue().toString(),Downloading_timer.getValue().toString(),thread_val,append_random_strings.isSelected()?"YES":"NO",save_image_data.isSelected()?"YES":"NO",save_images_in_different_folders.isSelected()?"YES":"NO",scraper_retries.getValue().toString(),downloader_retries.getValue().toString()}};
             for(int i=0;i<10;i++)
                 Properties.update(UpdatedKeysAndValues[0][i],UpdatedKeysAndValues[1][i]);//updates The Values
            Properties.Save();
        }


        @FXML
        public void threading_switch()
        {
            if(Threading.isSelected())
            {   Threading.setText("Enabled");
                total_threads.setDisable(false);
                total_threads.getSelectionModel().selectFirst();
            }
            else {
                Threading.setText("Disabled");
                total_threads.setDisable(true);
            }
            }

}