package ehu.isad.flickrKud;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.util.AuthStore;
import ehu.isad.Main;
import ehu.isad.db.ArgazkiDBKud;
import ehu.isad.db.BildumaDBKud;
import ehu.isad.db.ErabiltzaileDBKud;
import ehu.isad.flickr.FlickrAPI;
import ehu.isad.model.Argazkia;
import ehu.isad.model.ListaBildumak;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BildumaSortuKud implements Initializable {

    // Reference to the main application.
    private Main mainApp;

    @FXML
    private TextField bildumaIzena = new TextField();

    @FXML
    private TextField idArgazkia = new TextField();

    @FXML
    private Pane nagusia;

    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    @FXML
    public void onClickAtzera(ActionEvent actionEvent) throws Exception {
        System.out.println("close, baina ez dakit nola");

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void gordebotoia(ActionEvent actionEvent) {
        PhotosetsInterface pi = FlickrAPI.getInstantzia().getFlickr().getPhotosetsInterface();
        PhotosInterface ai = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
        try {
            Photoset berria = pi.create(bildumaIzena.getText(), null, idArgazkia.getText());
            BildumaDBKud.getInstantzia().bildumaSartu(bildumaIzena.getText(), berria.getId(), ErabiltzaileDBKud.getIdErab(), null);
            BildumaDBKud.getInstantzia().argazkiaBildumazAldatu(bildumaIzena.getText(), ArgazkiDBKud.getInstantzia().emanIdDB(idArgazkia.getText()));
            ListaBildumak.getNireBilduma().bildumaSartu(bildumaIzena.getText(), berria.getId());
            ArrayList<Argazkia> lista = new ArrayList<>();
            lista.add(ListaBildumak.getNireBilduma().argazkiaBilatu(idArgazkia.getText()));
            ListaBildumak.getNireBilduma().argazkiakSartu(lista, ListaBildumak.getNireBilduma().emanBildumaIzenarekin(bildumaIzena.getText()));

        } catch (FlickrException e) {
            e.printStackTrace();
            this.mainApp.bildumaSortuError();
        }
        this.mainApp.pantailaNagusiaErakutsi();
    }

    public void atzerabotoia(ActionEvent actionEvent) {
        this.mainApp.pantailaNagusiaErakutsi();
    }
}
