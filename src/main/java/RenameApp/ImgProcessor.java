package RenameApp;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ImgProcessor {
    //convention is large small completed home

    //home dir
    private Path home;

    //current file
    private Path currentFile;

    //paint dir
    private Path paintDir;
    //draw dir
    private Path drawDir;
    //sketch dir
    private Path sketchDir;

    //completed dir
    private Path completedDir;


    public Path getHome() {
        return home;
    }

    public void setHome(Path ahome) {
        home = ahome;
        //set up directory structure
        try {
            setUpDirStructure();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Path getCurrentFile() {
        return currentFile;
    }

    public void setCurrentFile(Path aCurrentFile) {
        currentFile = aCurrentFile;
    }


    //set up dir structure
    private void setUpDirStructure() throws IOException {
        // make a new project dir called web_project
//          -image dir (selected by user)
//                  -web_project
//                      -paint
//                          -large
//                          -small
//                      -draw
//                          -large
//                          -small
//                      -sketch
//                          -large
//                          -small
        paintDir = Files.createDirectories( home.resolve("paint/large") );
        Files.createDirectories( home.resolve("paint/small") );

        drawDir = Files.createDirectories( home.resolve("draw/large") );
        Files.createDirectories( home.resolve("draw/small") );

        sketchDir = Files.createDirectories( home.resolve("sketch/large") );
        Files.createDirectories( home.resolve("sketch/small") );

        completedDir = Files.createDirectories( home.resolve("completed") );
    }


    //save thumbnail
    protected void saveThumbnail() {
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
    protected void saveLarge() {
        // create command
        ConvertCmd cmd = new ConvertCmd();

        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage();
        op.filter("Lanczos");
        op.resize(350,500);
        op.strip();
        op.unsharp(0.0,.5,0.5, 0.05 );
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

    protected void moveCompletedToDone(Path currentFile){
        try {
            Files.move( currentFile, completedDir.resolve( currentFile.getFileName() ) );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //save image to folders
        //save thumbnail
        //save large
        //image moved to completed.


    //low priority
    //may need a text key csv -- old names, new names


}
