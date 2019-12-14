package ehu.isad.flickrKud.error;

import ehu.isad.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class SetPropErrorKud implements Initializable {

    private Main mainApp;

    @FXML
    private Button okBotoia;


    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    @FXML
    public void itxi(javafx.event.ActionEvent actionEvent) {
        this.mainApp.itxi();
    }

    //This method is called upon fxml load
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
