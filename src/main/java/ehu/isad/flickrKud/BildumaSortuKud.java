package ehu.isad.flickrKud;

import com.flickr4java.flickr.util.AuthStore;
import ehu.isad.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class BildumaSortuKud implements Initializable {

    // Reference to the main application.
    private Main mainApp;

    @FXML
    private TextField bildumaIzena;
    @FXML
    private Pane nagusia;

    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    @FXML
    public void onClickAtzera(ActionEvent actionEvent) throws Exception {
        System.out.println("close, baina ez dakit nola");

    }

    @FXML
    public void onClickGorde(ActionEvent actionEvent) throws Exception {
        bildumaSortu(bildumaIzena.getText());
    }

    private void bildumaSortu(String izena){

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
