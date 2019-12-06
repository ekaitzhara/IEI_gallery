package ehu.isad.flickrKud;

import com.flickr4java.flickr.util.AuthStore;
import ehu.isad.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BildumaSortuKud implements Initializable {

    // Reference to the main application.
    private Main mainApp;

    private boolean logout = false;

    @FXML
    private ComboBox hizkuntzAldaketa;

    private AuthStore authStore;



    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    @FXML
    public void onClickAtzera(ActionEvent actionEvent) throws Exception {
        System.out.println("gorde");

    }

    @FXML
    public void onClickGorde(ActionEvent actionEvent) throws Exception {
        System.out.println("gorde");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }

    public void logoutAktibatu() {
        this.logout = true;
    }
}
