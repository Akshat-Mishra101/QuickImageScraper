package Engine;

import javafx.concurrent.Task;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class FileImporter extends Task<Void>
{   String paths; //path to the different Files Selected
    public FileImporter(String path)
    {
       this.paths=path;

    }
    @Override
    protected Void call() throws Exception {
        // Code To Import The Files
        FileWriter fw=new FileWriter("temp.links");
        fw.close();
        Scanner sc=new Scanner(paths);
        updateMessage("Loading Files");
        updateProgress(-1,100);
        while(sc.hasNext())
        {
            Scanner sd=new Scanner(new File(sc.nextLine()));
            while(sd.hasNext())
            {
                String line=sd.nextLine()+"\r\n";

                Files.write(Paths.get("temp.links"), line.getBytes(), StandardOpenOption.APPEND);//dumps the data to the file on our disk
                Properties.total_links++; //increments the total links counter
            }

        }
     updateMessage("Files Loaded Successfully");
        updateProgress(0,100);
        return null;
    }
}
