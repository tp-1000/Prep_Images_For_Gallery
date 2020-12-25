package RenameApp;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.nio.file.Path;

public class Controller {

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

    //used for name binding
    SimpleStringProperty catValue;
    SimpleStringProperty mediumValue;
    SimpleStringProperty placeValue;
    SimpleStringProperty heightValue = new SimpleStringProperty();
    SimpleStringProperty widthValue = new SimpleStringProperty();
    SimpleStringProperty nameValue = new SimpleStringProperty();


    public void initialize() {
        StringBinding nameString;
        
        //cat group listener
        catValue = new SimpleStringProperty( cat.getSelectedToggle().getUserData().toString() );

        cat.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                if (cat.getSelectedToggle() != null) {
                    catValue.setValue( cat.getSelectedToggle().getUserData().toString() );
                    disableToggles();
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
        StringExpression nameBind = Bindings.concat( catValue,mediumValue,placeValue
                ,heightValue.concat("_"),widthValue.concat("_"),nameValue);
        //string builder

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

    //auto-gen name
        //if clicked disable name input
        //create name based off getting file count in selected cat dir
//private void autoGenName() {
//    if (nameDTO.getPartType().equals("d")) {
//        int numberOfFiles = ImageProcessor.getDirDrawLargeLocation().toFile().list().length + 1;
//        nameDTO.setPartName(nameDTO.getPartName().concat("_Drawing_").concat(String.valueOf(numberOfFiles)));
//    } else if (nameDTO.getPartType().equals("s")) {
//        int numberOfFiles = ImageProcessor.getDirDrawLargeLocation().toFile().list().length + 1;
//        nameDTO.setPartName(nameDTO.getPartName().concat("_Sketch_").concat(String.valueOf(numberOfFiles)));
//    } else if (nameDTO.getPartType().equals("p")) {
//        int numberOfFiles = ImageProcessor.getDirPaintLargeLocation().toFile().list().length + 1;
//        if (nameDTO.getPartEnv().equals("y")) {
//            nameDTO.setPartName(nameDTO.getPartName().concat("_Plein_Air_").concat(String.valueOf(numberOfFiles)));
//        } else if (nameDTO.getPartType().equals("o")) {
//            nameDTO.setPartName(nameDTO.getPartName().concat("_Oil_").concat(String.valueOf(numberOfFiles)));
//        } else if (nameDTO.getPartType().equals("w")) {
//            nameDTO.setPartName(nameDTO.getPartName().concat("_Watercolor_").concat(String.valueOf(numberOfFiles)));
//        }
//    }
//}
    //save
        //uses new name
        //new thread for image process? (most likely process image class extends thread-able)
        //files get resample and go to small/large
        //files go to correct dir
        //resume thread

    //parseName
        //name is a combination of active radios and name input

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



}
