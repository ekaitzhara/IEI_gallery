package ehu.isad.controller;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.util.AuthStore;
import ehu.isad.Main;
import ehu.isad.controller.flickr.FlickrAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class KautotuKud implements Initializable {

  // Reference to the main application.
  private Main mainApp;

  @FXML
  private ComboBox hizkuntzAldaketa;

  private AuthStore authStore;



  public void setMainApp(Main main) {
    this.mainApp = main;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) throws IOException, FlickrException {

    RequestContext rc = RequestContext.getRequestContext();
    FlickrAPI fs = FlickrAPI.getInstantzia();

    this.authStore = fs.getAuthStore();

    if (this.authStore != null) {
        Auth auth = this.authStore.retrieve(fs.getNsid());
        if (auth == null) {
            this.authorize(); // throws Exception
        } else {
            rc.setAuth(auth);
            this.mainApp.mainErakutsi();
          }
    }

  }

    private void authorize() throws IOException, FlickrException {
        mainApp.kautotu2zatiaerakutsi();
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
      List<String> autobusak = new ArrayList<>();
      autobusak.add("Euskara");
      autobusak.add("Español");
      autobusak.add("English");


      hizkuntzAldaketa.getItems().addAll(autobusak);

  }

}