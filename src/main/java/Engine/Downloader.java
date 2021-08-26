package Engine;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ListView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class Downloader extends Task<Void> {
    ListView lv;
    String path;
    public Downloader(ListView lv,String path)
    {

        this.lv=lv;
        this.path=path;
    }
    @Override
    protected Void call() throws Exception {
        System.out.println("here too");
        if(Properties.get("save").equals("YES")) {
            //If The Data Saving Property Is Enabled, We create The File
            FileWriter fw = new FileWriter("LinksAndAlts.csv");//The Data Is Saved In A CSV File
            fw.close();
            Files.write(Paths.get("LinksAndAlts.csv"), ("Page-Link,SRC,Data-SRC,Title,Image-Alt,Width,Height"+"\r\n").getBytes(), StandardOpenOption.APPEND);

        }

        DownloaderEngine de=new DownloaderEngine("Images/",6);
        Scanner sc=new Scanner(new File(path));//Loads The File

        int current_link_number=0;
        while(sc.hasNext())
        {
            updateProgress(current_link_number++,Properties.total_links);//Updates The Progress Bar

            String link=sc.nextLine();
            System.out.println("even here");


            //Wrap This Document Object Around A Retry Mechanism, which retries as many times as defined in our configuration file

            String finallink=Properties.get("proxy")+(Properties.get("encode").equals("NO")?link: URLEncoder.encode(link, StandardCharsets.UTF_8));
            System.out.println(finallink+" is this");

            Document doc= Jsoup.connect(Properties.get("proxy")+(Properties.get("encode").equals("NO")?link: URLEncoder.encode(link, StandardCharsets.UTF_8))).timeout(Integer.parseInt(Properties.get("timeout"))).get();//Attempt To Scrape The Article
            System.out.println("NOT HERE");
            Elements images=doc.getElementsByTag("img");
            String image_array[]=new String[images.size()];
            updateMessage("Scraping "+getAFolderName(link)+" Total Images "+images.size());
            int position=0;
            /**
             * Here We Take The Different Attributes Of an Image
             * and save them in a csv file
             */
            for(Element image:images)
            {
                 String src=image.attr("src").trim().length()>0?image.attr("src").trim():"N/A";
                 String data_src=image.attr("data-src").trim().length()>0?image.attr("data-src").trim():"N/A";
                 String title=image.attr("title").trim().length()>0?image.attr("title").trim():"N/A";
                 String alt=image.attr("alt").trim().length()>0?image.attr("alt").trim():"N/A";
                 String width=image.attr("width").trim().length()>0?image.attr("width").trim():"N/A";
                 String height=image.attr("height").trim().length()>0?image.attr("height").trim():"N/A";

              System.out.println(image);
                 //The line below Adds To Our CSV File
                if(Properties.get("save").equals("YES"))//Saves If The Property Is Enabled
                Files.write(Paths.get("LinksAndAlts.csv"), (link+","+src+","+data_src+","+title+","+alt+","+width+","+height+"\r\n").getBytes(), StandardOpenOption.APPEND);

                if(Properties.get("image_saving").equals("YES"))
                if(!(new File("Images/"+getAFolderName(src)).exists()))
                    new File("Images/"+getAFolderName(src)).mkdir();

                if(Properties.get("threads").equals("NO"))
                {
                    //download the image serially and save it
                    String result=SerialDownloader.SecureDownload(src,lv);
                    System.out.println("The result is "+result);


                    Platform.runLater(()->{  //updates the ListView UI

                        lv.getItems().add(src+(result.equals("S")?" Downloaded":" Could Not Download"));

                    });


                }
                else
                {
                   //collect the links in an array
                    image_array[position++]=src;
                }





            }
           if(!Properties.get("threading").equals("NO"))
           {
               if(Properties.get("threading").equals("2"))
               {



               }


           }
            //check if the link downloading was serial or parallel
            // if parallel, download the
        }
        updateMessage("Scraping Complete");
        return null;
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
}
