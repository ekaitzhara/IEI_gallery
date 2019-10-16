package ehu.isad.controller;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.util.AuthStore;
import ehu.isad.Main;
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

  private FlickrSortu fs;

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

    /*
    if ("Flickr".equals(comboZerbitzua.getValue()) &&
        "juanan".equals(txtErabiltzaile.getText()) &&
        "pereira".equals(txtPasahitza.getText())) {

      mainApp.mainErakutsi();
    }
     */


    RequestContext rc = RequestContext.getRequestContext();
    fs = new FlickrSortu();
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
        /*
        AuthInterface authInterface = fs.getFlickr().getAuthInterface();
        OAuth1RequestToken requestToken = authInterface.getRequestToken();

        String url = authInterface.getAuthorizationUrl(requestToken, Permission.WRITE); // hemen zehaztu baimenak
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        //mainApp.kautotuURLGorde(url);

         */
        mainApp.kautotu2zatiaerakutsi();

        /*
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        String tokenKey = new Scanner(System.in).nextLine();

        OAuth1Token accessToken = authInterface.getAccessToken(requestToken, tokenKey);

        Auth auth = authInterface.checkToken(accessToken);
        RequestContext.getRequestContext().setAuth(auth);
        this.authStore.store(auth);
        System.out.println("Thanks.  You probably will not have to do this every time.  Now starting backup.");

         */
    }


  @Override
  public void initialize(URL location, ResourceBundle resources) {
    comboZerbitzua.getItems().add(0,"Dropbox");
  }

}