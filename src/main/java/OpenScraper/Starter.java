package OpenScraper;

import Engine.Properties;

import java.io.File;
import java.io.FileWriter;
import java.util.Locale;


import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class Starter {
    public static void main(String args[])throws Exception{
       Properties.load();

        //Properties Module has Been Tested
       //File Importing Module Works Perfectly(though it's a bit slow and might require a speed upgrade in the near future)
        //Work And Complete And Test The Downloader Engine Today


        //MultiThread This App Tomorrow



       boolean isPresent=new File("Data").exists();
       boolean isImageFolderPresent=new File("Images").exists();
       if(!isImageFolderPresent)
           new File("Images").mkdir();
       if(!isPresent)
       {
           new File("Data").mkdir();// Makes The Directory Of The Name Data
           String DefaultkeysAndValues[][]={{"proxy","encode","timeout","dtimeout","threads","names","save","image_saving","sretry","dretry"},{"","NO","3000","3000","NO","NO","NO","NO","40","40"}};//Contains The Default Configuration Of The Key Value Pairs
           for(int i=0;i<10;i++)
               Properties.update(DefaultkeysAndValues[0][i],DefaultkeysAndValues[1][i]);
           Properties.Save();
       }
  Properties.load();

   String url_to_icon="";
       App.main(args);
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
}
