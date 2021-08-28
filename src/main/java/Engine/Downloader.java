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
import java.util.Arrays;
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
            updateProgress((current_link_number++/Properties.total_links)*100,100);//Updates The Progress Bar
System.out.println((current_link_number++/Properties.total_links)*100+" AND "+Properties.total_links);
            String link=sc.nextLine();
           


            //Wrap This Document Object Around A Retry Mechanism, which retries as many times as defined in our configuration file

            String finallink=Properties.get("proxy")+(Properties.get("encode").equals("NO")?link: URLEncoder.encode(link, StandardCharsets.UTF_8));
            System.out.println(finallink+" is this");
            Document doc=null;
            int retry_count=0;
            String resulto="";
            updateMessage("Attempting To Scrape "+finallink);
            while(retry_count!=Integer.parseInt(Properties.get("sretry"))) {
              
                try {
                    doc = Jsoup.connect(Properties.get("proxy") + (Properties.get("encode").equals("NO") ? link : URLEncoder.encode(link, StandardCharsets.UTF_8))).timeout(Integer.parseInt(Properties.get("timeout"))).get();//Attempt To Scrape The Article
                    resulto="s";

                    if(resulto.equals("s"))
                        break;
                } catch (Exception e) {
                     resulto="f";
                     retry_count++;
                }
            }
            updateMessage("Scraped Successfully!");
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
                    updateMessage("Accumulating Images For MultiThreaded Mode");
                    image_array[position++]=src;
                }





            }
           if(!Properties.get("threads").equals("NO"))
           {
               if(Properties.get("threads").equals("2"))
               {
                   updateMessage("Multi-Threaded Mode");
                int mid=image_array.length/2;


                Thread first_thread=new Thread(){
                    @Override
                    public void run()
                    {
                     for(int i=0;i<mid;i++)
                     {
                         DownloaderEngine dowloader_Engine=new DownloaderEngine();
                     String result=dowloader_Engine.SecureDownload(image_array[i],lv);
                         int newi=i;
                     if(result.equals("S"))
                     {

                         Platform.runLater(()->{

                             lv.getItems().add("Thread 1: Succesfully Downloaded: "+image_array[newi]);
                         });


                     }
                      else
                     {
                         Platform.runLater(()->{
                         lv.getItems().add("Thread 1: Failed To Download: "+image_array[newi]);
                         });

                     }
                     }
                    }
                };

                   Thread second_thread=new Thread(){
                       @Override
                       public void run()
                       {
                        for(int i=mid;i<image_array.length;i++)
                        {
                            DownloaderEngine dowloader_Engine=new DownloaderEngine();
                            String result=dowloader_Engine.SecureDownload(image_array[i],lv);
                            int newi=i;
                            if(result.equals("S"))
                            {
                                Platform.runLater(()-> {
                                    lv.getItems().add("Thread 2: Succesfully Downloaded: " + image_array[newi]);

                                });
                            }
                            else
                            {
                                Platform.runLater(()-> {
                                lv.getItems().add("Thread 2: Failed To Download: "+image_array[newi]);
                                });

                            }
                        }
                       }
                   };
                   first_thread.setDaemon(true);
                   second_thread.setDaemon(true);
                   first_thread.start();
                   second_thread.start();
               //
                   while(first_thread.isAlive()||second_thread.isAlive())
                   {
                      //wait for the threads to finish

                   }
                   System.out.println("Threads Complete");

               }
               else{
                   //n Thread Distribution
                   //Create an Array Of Threads


                   DownloaderEngine downloder_array[]=new DownloaderEngine[image_array.length];//Create As Many Downloder instances as the images
                   Thread thread_array[]=new Thread[image_array.length];
                   /**
                    * Initiating the downloader engines and enveloping them
                    * in threads
                    */
                   updateMessage("About To Download Images");
                   for(int i=0;i<image_array.length;i++)
                   {
                       downloder_array[i]=new DownloaderEngine(image_array[i],lv);
                       thread_array[i]=new Thread(downloder_array[i]);
                       thread_array[i].start();

                   }
                  updateMessage("Downloading Images");
                   while(EvenASingleThreadLives(thread_array))
                   {
                       //wait;
                   }
                  updateMessage("Download Complete");
               }


           }
            //check if the link downloading was serial or parallel
            // if parallel, download the
        }
        updateMessage("Scraping Complete");
        return null;
    }

    private boolean EvenASingleThreadLives(Thread[] thread_array) {

        int count_of_falses=0;
       for(int i=0;i<thread_array.length;i++)
       {
           if(!thread_array[i].isAlive())
               count_of_falses++;
       }
      if(count_of_falses==thread_array.length)
        return false;
      else
          return true;
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
