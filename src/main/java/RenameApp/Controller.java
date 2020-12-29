package RenameApp;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class Controller {

    @FXML private TextField originalName;
    @FXML private Text newName;
    @FXML private ToggleGroup cat;
    @FXML private ToggleGroup medium;
    @FXML private ToggleGroup place;
    @FXML private TextField height;
    @FXML private TextField width;
    @FXML private TextField name;
    @FXML private CheckBox generate;

    //disable toggle groups
    @FXML private HBox disable_medium;
    @FXML private HBox disable_place;

    //disable save
    @FXML private Button saveBtn;
    private SimpleBooleanProperty blockSaveBtn = new SimpleBooleanProperty(true);
    @FXML private Label nameWarning;

    //used for name binding
    SimpleStringProperty catValue;
    SimpleStringProperty mediumValue;
    SimpleStringProperty placeValue;
    SimpleStringProperty heightValue = new SimpleStringProperty();
    SimpleStringProperty widthValue = new SimpleStringProperty();
    SimpleStringProperty nameValue = new SimpleStringProperty();

    //stop-start key for controlling stream of images
    private final Object STOP_KEY = new Object();

    //image
    private final ImgProcessor imgProcessor = new ImgProcessor();
    @FXML private ImageView previewImage;

    //parseName set up
    //name is a combination of active radios and name input
    public void initialize() {


        //disable save button when no files
        saveBtn.disableProperty().bind(blockSaveBtn);

        //name listener - if there is no input
        newName.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                                String old_toggle, String new_toggle) {
                if ( imgProcessor.fileExists(newName.getText()) ) {
//                    note displayed saying non unique
    //                    disable save btn
                            blockSaveBtn.setValue(true);
                            nameWarning.setText("file with that name already exists");
//                        System.out.println("file exists");
                } else if (imgProcessor.getHome() != null){
                    //clear note and set block to false
                    blockSaveBtn.setValue(false);
                    nameWarning.setText(null);
                } else {
                    nameWarning.setText(null);
                }

            }
        });


        //cat group listener
        catValue = new SimpleStringProperty( cat.getSelectedToggle().getUserData().toString() );


        cat.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (cat.getSelectedToggle() != null) {
                    catValue.setValue( cat.getSelectedToggle().getUserData().toString() );
                    disableToggles();

                    //changes to the same value to activate listener
                    name.textProperty().setValue(name.textProperty().getValue());
                }
            }
        });


        //medium group listener
        mediumValue = new SimpleStringProperty( medium.getSelectedToggle().getUserData().toString() );

        medium.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (medium.getSelectedToggle() != null) {
                    mediumValue.setValue( medium.getSelectedToggle().getUserData().toString() );

                    //changes to the same value to activate listener
                    name.textProperty().setValue(name.textProperty().getValue());

                }
            }
        });

        //place group listener
        placeValue = new SimpleStringProperty( place.getSelectedToggle().getUserData().toString() );

        place.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (place.getSelectedToggle() != null) {
                    placeValue.setValue( place.getSelectedToggle().getUserData().toString() );
                }
            }
        });


        //height binding
        heightValue.bind(height.textProperty());
        //width binding
        widthValue.bind(width.textProperty());
        //name binding
        nameValue.bind(name.textProperty());
        nameValue.bind( Bindings.when( name.textProperty().isNotEmpty() ).then( name.textProperty() ).otherwise( Bindings.createStringBinding( genNameBinding(), nameValue ) ) );
        //string builder
        StringExpression nameBind = Bindings.concat( catValue,mediumValue,placeValue
                ,heightValue.concat("X"),widthValue.concat("_"),nameValue.concat(".jpeg"));


        newName.textProperty().bind(nameBind);
    }


    //pickDirectory
        //directory selected
        //new thread
        //directory stream
        //loop through stream
        //set element to preview image
        //set original name to original name
        //stop thread
        //alert finished --- exiting
  @FXML  private void dirPickerDialog(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select a folder");
        File selectedDir  = directoryChooser.showDialog(null);
        //set home dir
        imgProcessor.setHome(selectedDir.toPath());

        //pause thread
        Platform.runLater( ()-> {
            blockSaveBtn.setValue(false);
            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(selectedDir.toPath(), "*.{jpg,JPG,jpeg,JPEG}")){
                //for loop of stream to get next image file...
                for(Path filePath : dirStream){
                    name.requestFocus();
                    imgProcessor.setCurrentFile(filePath);
                    setPreviewImgAndName(filePath);

                    //empty name input activate listener
                    name.textProperty().setValue("");
                    Platform.enterNestedEventLoop(STOP_KEY);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            blockSaveBtn.setValue(true);
            previewImage.setImage(null);
            originalName.setText("");
        });
        }


    //save
        //uses new name
        //new thread for image process? (most likely process image class extends thread-able)
        //files get resample and go to small/large
        //files go to correct dir
        //resume thread
 @FXML private void save(){
        imgProcessor.save(newName.getText());
        Platform.exitNestedEventLoop(STOP_KEY, null);
    }

    //disable extra radios if !paint radio selected
    private void disableToggles() {
        if(! cat.getSelectedToggle().getUserData().toString().equals("p") ) {
            //disables
            disable_medium.setDisable(true);
            disable_place.setDisable(true);
            //nulls their respective values
            mediumValue.setValue("x");
            placeValue.setValue("x");
        } else {
            disable_medium.setDisable(false);
            disable_place.setDisable(false);
            mediumValue.setValue( medium.getSelectedToggle().getUserData().toString() );
            placeValue.setValue( place.getSelectedToggle().getUserData().toString() );
        }

    }

    //set preview image + original name
    private void setPreviewImgAndName(Path filePath) {
        try {
            previewImage.setImage(new Image(filePath.toUri().toURL().toString(), true));
            previewImage.setFitWidth(400);
            previewImage.setPreserveRatio(true);
            previewImage.setSmooth(true);
            previewImage.setCache(true);
            //name
            originalName.setText( filePath.getFileName().toString() );

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }



    private Callable<String> genNameBinding(){
            return () -> {
            String name = "Untitled";
            if( imgProcessor.getHome() != null ) {
                char charKey = catValue.getValue().charAt(0);
                int fileCount;
                switch (charKey) {
                    case 'p':
                        fileCount = imgProcessor.getPaintDir().toFile().list().length;
                        if (mediumValue.getValue().charAt(0) == 'o') {
                            name = "Untitled_Oil_" + fileCount;
                        } else {
                            name = "Untitled_WaterColor_" + fileCount;
                        }
                        break;
                    case 'd':
                        fileCount = imgProcessor.getDrawDir().toFile().list().length;
                        name = "Untitled_Drawing_" + fileCount;
                        break;
                    case 's':
                        fileCount = imgProcessor.getSketchDir().toFile().list().length;
                        name = "Untitled_Sketch_" + fileCount;
                        break;
                }
            }
            return name;
        };
    }




}
