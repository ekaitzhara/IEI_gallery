package ehu.isad.flickrKud.error;

import ehu.isad.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.ResourceBundle;

public class ZerbaitKlikaturikKud implements Initializable {

    private Main mainApp;

    @FXML
    private Label zerbaitKlikatuta1 = new Label();

    @FXML
    private Label zerbaitKlikatuta2 = new Label();


    public void setMainApp(Main main) {
        this.mainApp = main;
    }



    //This method is called upon fxml load
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void jarriEditaturikTestua() {
        this.zerbaitKlikatuta1.setText("Zerbait editatu egin duzu eta ez duzu gorde,");
        this.zerbaitKlikatuta2.setText("mesedez, gorde edo deuseztatu egin dituzun aldaketak");
    }
}
