package ehu.isad.flickrKud.error;

import ehu.isad.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ZerbitzurikEzKud implements Initializable {
    private Main mainApp;


    @FXML
    private Label zerbaitzurikEz1 = new Label();

    @FXML
    private Label zerbaitzurikEz2 = new Label();


    public void setMainApp(Main main) {
        this.mainApp = main;
    }



    //This method is called upon fxml load
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void atzeraBota(ActionEvent actionEvent) {
        this.mainApp.kautoketaraEraman();
    }
}
