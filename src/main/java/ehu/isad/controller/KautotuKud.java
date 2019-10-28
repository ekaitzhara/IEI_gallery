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
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class KautotuKud implements Initializable {

  // Reference to the main application.
  private Main mainApp;

  private AuthStore authStore;


  @FXML
  private ComboBox comboZerbitzua;

  @FXML
  private TextField txtErabiltzaile;

  @FXML
  private TextField txtPasahitza;

  public void setMainApp(Main main) {
    this.mainApp = main;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) throws IOException, FlickrException {
    System.out.println(txtErabiltzaile.getText() + ":" + txtPasahitza.getText());
    System.out.println(comboZerbitzua.getValue());


    RequestContext rc = RequestContext.getRequestContext();
    FlickrAPI fs = FlickrAPI.getInstantzia();
    this.authStore = fs.getAuthStore();

    if (this.authStore != null) {
        Auth auth = this.authStore.retrieve(fs.getNsid());
        if (auth == null) {
            this.authorize(); // throws Exception
        } else {
            rc.setAuth(auth);
          }
    }
  }

    private void authorize() throws IOException, FlickrException {
        mainApp.kautotu2zatiaerakutsi();
    }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

  }
}