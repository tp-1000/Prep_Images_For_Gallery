package RenameApp;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.IOException;
import java.nio.file.Path;

public class ImgProcessor {
    //convention is large small completed home

    //home dir

    //current file
    private static Path currentFile;

    //paint dir
    //draw dir
    //sketch dir

    //completed dir




    public static Path getCurrentFile() {
        return currentFile;
    }

    public static void setCurrentFile(Path currentFile) {
        ImgProcessor.currentFile = currentFile;
    }






    //set up dir structure

    //save thumbnail
    protected static void saveThumbnail() {
            // create command
            ConvertCmd cmd = new ConvertCmd();

            // create the operation, add images and operators/options
            IMOperation op = new IMOperation();
            op.addImage();
            op.filter("Lanczos");
            op.resize(350,500);
            op.strip();
            op.unsharp(0.0,.6,0.5, 0.02 );
            op.quality(80.0);
            op.addImage();

        try {
            cmd.run(op,currentFile.toString(), currentFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }

    }

    //save large

    //save image to folders
        //save thumbnail
        //save large
        //image moved to completed.

    //


    //low priority
    //may need a text key csv -- old names, new names


}
