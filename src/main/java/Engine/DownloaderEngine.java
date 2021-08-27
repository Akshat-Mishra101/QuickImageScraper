package Engine;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * Used To Download The Images Parallely
 */
public class DownloaderEngine implements Runnable {
    String path;//Path To Your Downloads Folder
    int timeout;//The TimeOut For Web Scraping
    public DownloaderEngine(String path,int timeout)
    {

        this.path=path;
        this.timeout=timeout;


    }
    public DownloaderEngine()
    {


    }
    String link;
    ListView lv;
    public DownloaderEngine(String link,ListView lv)
    {
        this.link=link;
        this.lv=lv;
    }
    /**
     * The following function takes, three arguments
     * The url of the image to be downloaded, the timeout under which it has to be downloaded, and the name with which it has to be saved
     * The Fourth parameter kis for when the bot fails to download an image and it defines the number of retries that must occur for the image
     * @param url
     * @param timeout
     * @param name
     */
    public void download(String url,int timeout,String name, int RetryCount)
    {



    }
    public String SecureDownload()
    {
        String report="";
        // This will get input data from the server
        InputStream inputStream = null;
        String prop=Properties.get("save_images");
        String Folder_addition=getAFolderName(link)+"/";


        System.out.println("detet folder name and then save it");

        String destName ="Images/"+Folder_addition+"/"+getAName(link);

        // This will read the data from the server;



        OutputStream outputStream = null;
        int retry_count=0;

        while(retry_count!=Integer.parseInt(Properties.get("sretry"))) //loop to implement the Retry Mechanism
        {       System.out.println("Retry Count "+retry_count+" Report is "+report+" OO "+report.equals("S"));
            try {
                // This will open a socket from client to server


                URL url = new URL(Properties.get("proxy")+(Properties.get("encode").equals("YES")? URLEncoder.encode(link, StandardCharsets.UTF_8):link));


                // This user agent is for if the server wants real humans to visit


                // This socket type will allow to set user_agent
                URLConnection con = url.openConnection();

                con.setConnectTimeout(Integer.parseInt(Properties.get("dtimeout")));//timeout has been changed to 60 seconds, this might get rid of the corrupted images problem
                // Setting the user agent


                //Getting content Length


                // Requesting input data from server
                inputStream = con.getInputStream();

                // Open local file writer
                outputStream = new FileOutputStream(destName);

                // Limiting byte written to file per loop
                byte[] buffer = new byte[2048];

                // Increments file size
                int length;
                int downloaded = 0;

                // Looping until server finishes
                while ((length = inputStream.read(buffer)) != -1) {
                    // Writing data
                    outputStream.write(buffer, 0, length);
                    downloaded += length;
                    //System.out.println("Downlad Status: " + (downloaded * 100) / (contentLength * 1.0) + "%");


                }
                outputStream.close();
                inputStream.close();
                report = "S";
                String report_copy=report;
                Platform.runLater(()->{
                    lv.getItems().add(report_copy.equals("S")?"Successfully Downloaded: "+link:"Failed To Download: "+link);
                });
                if(report.equals("S"))
                    break;


            } catch (Exception ex) {
                report = ex.toString();
                retry_count++;
                System.out.println(ex + " is the exception");
            }
        }

        return report;


    }
    public String SecureDownload(String link, ListView lv)
    {

        String report="";
        // This will get input data from the server
        InputStream inputStream = null;
        String prop=Properties.get("save_images");
        String Folder_addition=getAFolderName(link)+"/";


        System.out.println("detet folder name and then save it");

        String destName ="Images/"+Folder_addition+"/"+getAName(link);

        // This will read the data from the server;



        OutputStream outputStream = null;
        int retry_count=0;

        while(retry_count!=Integer.parseInt(Properties.get("sretry"))) //loop to implement the Retry Mechanism
        {       System.out.println("Retry Count "+retry_count+" Report is "+report+" OO "+report.equals("S"));
            try {
                // This will open a socket from client to server


                URL url = new URL(Properties.get("proxy")+(Properties.get("encode").equals("YES")? URLEncoder.encode(link, StandardCharsets.UTF_8):link));


                // This user agent is for if the server wants real humans to visit


                // This socket type will allow to set user_agent

                URLConnection con = url.openConnection();

                con.setConnectTimeout(Integer.parseInt(Properties.get("dtimeout")));//timeout has been changed to 60 seconds, this might get rid of the corrupted images problem
                // Setting the user agent


                //Getting content Length


                // Requesting input data from server
                inputStream = con.getInputStream();

                // Open local file writer
                outputStream = new FileOutputStream(destName);

                // Limiting byte written to file per loop
                byte[] buffer = new byte[2048];

                // Increments file size
                int length;
                int downloaded = 0;

                // Looping until server finishes
                while ((length = inputStream.read(buffer)) != -1) {
                    // Writing data
                    outputStream.write(buffer, 0, length);
                    downloaded += length;
                    //System.out.println("Downlad Status: " + (downloaded * 100) / (contentLength * 1.0) + "%");


                }
                outputStream.close();
                inputStream.close();
                report = "S";
                if(report.equals("S"))
                    break;


            } catch (Exception ex) {
                report = ex.toString();
                retry_count++;
                System.out.println(ex + " is the exception");
            }
        }

        return report;

    }
    private static String getAFolderName(String s)
    {
        boolean result=Properties.get("image_saving").equals("YES")?true:false;

        String folder_name="";
        if(result)
        {
            try{folder_name=s.split("/")[2];}
            catch(Exception e){}
        }
        String resultant=folder_name.length()>0?folder_name:randomAlphabetic(10).toUpperCase();
        return result?resultant:"";
    }

    private static String getAName(String link) {

        String greatest_slug=link.substring(link.lastIndexOf("/")+1);
        String appended_string=Properties.get("names").equals("YES")?randomAlphabetic(10).toUpperCase():"";//Appends Random Code When The Option Is Enabled
        return appended_string+(greatest_slug.length()>0?greatest_slug:randomAlphabetic(10).toUpperCase());
    }

    @Override
    public void run() {
        SecureDownload();
    }
}
