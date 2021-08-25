package Engine;

import javafx.concurrent.Task;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

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
    public String SecureDownload(String name)  {
        String report="";
        // This will get input data from the server
        InputStream inputStream = null;
        String destName ="";


        // This will read the data from the server;



        OutputStream outputStream = null;

        try {
            // This will open a socket from client to server


            URL url = new URL("");



            // This user agent is for if the server wants real humans to visit


            // This socket type will allow to set user_agent
            URLConnection con = url.openConnection();

            con.setConnectTimeout(200000);//timeout has been changed to 60 seconds, this might get rid of the corrupted images problem
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
            while ((length = inputStream.read(buffer)) != -1)
            {
                // Writing data
                outputStream.write(buffer, 0, length);
                downloaded+=length;
                //System.out.println("Downlad Status: " + (downloaded * 100) / (contentLength * 1.0) + "%");


            }
            outputStream.close();
            inputStream.close();
            report="S";

        } catch (Exception ex) {
            report=ex.toString();

            System.out.println(ex+" is the exception");


        }


        return report;
    }

    @Override
    public void run() {

    }
}
