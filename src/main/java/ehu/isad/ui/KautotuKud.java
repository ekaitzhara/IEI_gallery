package ehu.isad.ui;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.util.AuthStore;
import ehu.isad.Main;
import ehu.isad.flickr.FlickrAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class KautotuKud implements Initializable {

  // Reference to the main application.
  private Main mainApp;

  private boolean logout = false;

  @FXML
  private ComboBox hizkuntzAldaketa;

  @FXML
  private ComboBox zerbitzua;

  @FXML
  private Label zerbitzuaEzDago = new Label();



  private AuthStore authStore;



  public void setMainApp(Main main) {
    this.mainApp = main;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) throws IOException, FlickrException {

    RequestContext rc = RequestContext.getRequestContext();
    FlickrAPI fs = FlickrAPI.getInstantzia();

    this.authStore = fs.getAuthStore();

    if ("Flickr".equals(zerbitzua.getValue())) {
          zerbitzuaEzDago.setText("");
          if (this.authStore != null) {
              Auth auth = this.authStore.retrieve(fs.getNsid());
              if (auth == null || logout == true) {
                  this.logout = false;
                  this.authorize(); // throws Exception
              } else {
                  rc.setAuth(auth);
                  this.mainApp.pantailaNagusiaErakutsi();
              }
          }
      } else {
          System.out.println("Zerbitzu hau ez dago eskuragarri!!!");
          zerbitzuaEzDago.setText("Zerbitzu hau ez dago eskuragarri!!!");
          // EGIN PANTAILA ESATEN DUENA
      }
  }

    private void authorize() throws IOException, FlickrException {
        mainApp.kautotuFlickrErakutsi();
    }

    @FXML
    public void onClickHizkuntza(ActionEvent actionEvent) throws Exception {
        String laburdura = null;
        String herrialdea = null;
        if ("Euskara".equals(hizkuntzAldaketa.getValue())) {
            laburdura = "eu";
            herrialdea = "ES";
        } else if ("Español".equals(hizkuntzAldaketa.getValue())) {
            laburdura = "es";
            herrialdea = "ES";
        } else if ("English".equals(hizkuntzAldaketa.getValue())) {
            laburdura = "en";
            herrialdea = "UK";
        }
        System.out.println(laburdura);
        this.mainApp.hizkuntzaAldatu(laburdura, herrialdea);
    }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
      List<String> hizkuntzak = new ArrayList<>();
      hizkuntzak.add("Euskara");
      hizkuntzak.add("Español");
      hizkuntzak.add("English");


      hizkuntzAldaketa.getItems().addAll(hizkuntzak);

      List<String> zerbitzuak = new ArrayList<>();
      zerbitzuak.add("Google Fotos");
      zerbitzuak.add("Flickr");
      zerbitzuak.add("Instagram");

      zerbitzua.getItems().addAll(zerbitzuak);



  }

    public void logoutAktibatu() {
      this.logout = true;
    }
}