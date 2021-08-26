package Engine;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

public class Properties {

    public static int total_links=0;
    static String proxy="";
    static String url_encoding="";
    static String timeout="";//tested
    static String downloader_timeout="";//tested
    static String multithreading="";
    static String image_naming_policy="";//tested
    static String save_images_with_alts="";//tested
    static String save_images_in_folders=""; //tested
    static String scraper_retries="";//-> has to be implemented
    static String downloader_retries="";//tested
    public static String get(String key)
    {
     if(key.equals("proxy"))
         return proxy;
     else if(key.equals("encode"))
         return url_encoding;
     else if(key.equals("timeout"))
         return timeout;
     else if(key.equals("threads"))
         return multithreading;
     else if(key.equals("names"))
         return image_naming_policy;
     else if(key.equals("save"))
         return save_images_with_alts;
     else if(key.equals("image_saving"))
         return save_images_in_folders;
     else if(key.equals("dtimeout"))
         return downloader_timeout;
     else if(key.equals("sretry"))
         return scraper_retries;
     else if(key.equals("dretry"))
         return downloader_retries;
     else
         return "INVALID-KEY";

    }
    public static void load()throws Exception
    {
       Scanner sc=new Scanner(new File("Data/config.conf"));
       while(sc.hasNext())
       {
           String line=sc.nextLine();
           String key=line.substring(0,line.indexOf(":"));
           String value=line.substring(line.indexOf(":")+1);
           if(key.equals("PROXY"))
               proxy=value;
           if(key.equals("SCRAPER-TIMEOUT"))
               timeout=value;
           if(key.equals("DOWNLOADER-TIMEOUT"))
               downloader_timeout=value;
           if(key.equals("ENCODING"))
              url_encoding=value;
           if(key.equals("THREADING"))
               multithreading=value;
           if(key.equals("IMAGE-NAMING"))
               image_naming_policy=value;
           if(key.equals("CSV-FILE"))
               save_images_with_alts=value;
           if(key.equals("FOLDERS"))
               save_images_in_folders=value;
           if(key.equals("SRETRY"))
               scraper_retries=value;
           if(key.equals("DRETRY"))
               downloader_retries=value;



       }

    }
    public static void update(String key,String value)
    {
        if(key.equals("proxy"))
            proxy=value;
        else if(key.equals("encode"))
           url_encoding=value;
        else if(key.equals("timeout"))
            timeout=value;
        else if(key.equals("dtimeout"))
            downloader_timeout=value;
        else if(key.equals("threads"))
           multithreading=value;
        else if(key.equals("names"))
            image_naming_policy=value;
        else if(key.equals("save"))
            save_images_with_alts=value;
        else if(key.equals("image_saving"))
            save_images_in_folders=value;
        else if(key.equals("sretry"))
            scraper_retries=value;
        else if(key.equals("dretry"))
            downloader_retries=value;
    }
    public static void Save()throws Exception
    {
        FileWriter fw=new FileWriter("Data/config.conf");
        String file="PROXY:"+proxy+"\r\n"+"" +
                "ENCODING:"+url_encoding+"\r\n"+"" +
                "SCRAPER-TIMEOUT:"+timeout+"\r\n"+"" +
                "DOWNLOADER-TIMEOUT:"+downloader_timeout+"\r\n"+"" +
                "THREADING:"+multithreading+"\r\n"+"" +
                "IMAGE-NAMING:"+image_naming_policy+"\r\n"+"" +
                "CSV-FILE:"+save_images_with_alts+"\r\n"+"" +
                "FOLDERS:"+save_images_in_folders+"\r\n" +
                "SRETRY:"+scraper_retries+"\r\n" +
                "DRETRY:"+downloader_retries;
        fw.write(file);
        fw.close();
    }
}
