package RenameApp;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;
import java.nio.file.*;

import static java.lang.System.nanoTime;

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


    //reference file
    private Path reference;


    public Path getReference() {
        return reference;
    }

    public void setReference(Path reference) {
        this.reference = reference;
    }

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

    public Path getPaintDir() {
        return paintDir;
    }

    public void setPaintDir(Path paintDir) {
        this.paintDir = paintDir;
    }

    public Path getDrawDir() {
        return drawDir;
    }

    public void setDrawDir(Path drawDir) {
        this.drawDir = drawDir;
    }

    public Path getSketchDir() {
        return sketchDir;
    }

    public void setSketchDir(Path sketchDir) {
        this.sketchDir = sketchDir;
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

        reference = Paths.get( home.resolve("reference.csv").toString() );
        Files.createFile(reference);
    }


    //save thumbnail
    private void saveThumbnail(String name) {
            // create command
            ConvertCmd cmd = new ConvertCmd();

            // create the operation, add images and operators/options
            IMOperation op = new IMOperation();
            op.addImage();
            op.filter("Lanczos");
            op.resize(320,1500);
            op.strip();
            op.unsharp(0.0,.6,0.5, 0.02 );
            op.quality(80.0);
            op.addImage();

        try {
            cmd.run( op,currentFile.toString(), home.resolve( getCatDir(name)+"/small/"+name ).toString() );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }

    }

    //save large
    private void saveLarge(String name) {
        // create command
        ConvertCmd cmd = new ConvertCmd();

        // create the operation, add images and operators/options
        IMOperation op = new IMOperation();
        op.addImage();
        op.filter("Lanczos");
        op.resize(1200,2500);
        op.strip();
        op.unsharp(0.0,.5,0.5, 0.05 );
        op.quality(80.0);
        op.addImage();

        try {
            cmd.run( op,currentFile.toString(), home.resolve( getCatDir(name)+"/large/"+name ).toString() );
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IM4JavaException e) {
            e.printStackTrace();
        }

    }



    private void moveCompletedToDone(){
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
    protected void save(String name) {

        long start = System.nanoTime();
        System.out.println(Thread.currentThread().getName());
        saveLarge(name);
        saveThumbnail(name);
        moveCompletedToDone();
        String content = String.format("%s,%s\r\n", currentFile.getFileName(), name);

        try {
            Files.write(reference,
                    content.getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        System.out.println("... "+ (end - start)/1000000 );

    }

    public boolean fileExists(String name) {
        return home!=null?Files.exists( home.resolve( getCatDir(name)+"/large/"+name ) ):false;
    }

    private String getCatDir(String name) {
        char cat = name.charAt(0);

        String catDir = "";
        switch (cat) {
            case 'p':
                catDir = "paint";
                break;
            case 'd':
                catDir = "draw";
                break;
            case 's':
                catDir = "sketch";
                break;
        }
        return catDir;
    }
    //low priority
    //may need a text key csv -- old names, new names


}
