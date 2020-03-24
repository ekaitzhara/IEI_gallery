package ehu.iei.flickrKud.error;

import ehu.isad.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class ZerbaitKlikaturikKud implements Initializable {

    private Main mainApp;

    private static ResourceBundle bundle;

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

    public void jarriEditaturikTestua(String hizkuntza, String hizkuntzHerrialdea) {

        String z1 = (String) bundle.getObject("zerbaitEditatuta1");
        String z2 = (String) bundle.getObject("zerbaitEditatuta2");
        this.zerbaitKlikatuta1.setText(z1);
        this.zerbaitKlikatuta2.setText(z2);
    }

    public void jarriBildumaErrorTestua(String hizkuntza, String hizkuntzHerrialdea) {

        String z1 = (String) bundle.getObject("bildumaEzabatuError1");
        String z2 = (String) bundle.getObject("bildumaEzabatuError2");
        this.zerbaitKlikatuta1.setText(z1);
        this.zerbaitKlikatuta2.setText(z2);
    }

    public void syncEginTestua(String hizkuntza, String hizkuntzHerrialdea) {

        String z1 = (String) bundle.getObject("syncEginTestu1");
        String z2 = (String) bundle.getObject("syncEginTestu2");
        this.zerbaitKlikatuta1.setText(z1);
        this.zerbaitKlikatuta2.setText(z2);
    }

    public void jarriArgazkiEditatuErrorTestua(String hizkuntza, String hizkuntzHerrialdea) {
        String z1 = (String) bundle.getObject("argazkiaEditatuError1");
        String z2 = (String) bundle.getObject("argazkiaEditatuError2");
        this.zerbaitKlikatuta1.setText(z1);
        this.zerbaitKlikatuta2.setText(z2);
    }

    public void jarriEguneraketakBegiratuTestua(String hizkuntza, String hizkuntzHerrialdea) {
        String z1 = (String) bundle.getObject("checkUpdates1");
        String z2 = (String) bundle.getObject("checkUpdates2");
        this.zerbaitKlikatuta1.setText(z1);
        this.zerbaitKlikatuta2.setText(z2);
    }

    public void jarriAboutTestua(String hizkuntza, String hizkuntzHerrialdea) {
        String z1 = (String) bundle.getObject("about1");
        String z2 = (String) bundle.getObject("about2");
        this.zerbaitKlikatuta1.setText(z1);
        this.zerbaitKlikatuta2.setText(z2);
    }

    public void jarriBildumaSortuErrorTestua(String hizkuntza, String hizkuntzHerrialdea) {
        String z1 = (String) bundle.getObject("bildumaError1");
        String z2 = (String) bundle.getObject("bildumaError2");
        this.zerbaitKlikatuta1.setText(z1);
        this.zerbaitKlikatuta2.setText(z2);
    }

    public void jarriBundle(ResourceBundle pBundle) {
        bundle = pBundle;
    }

}
