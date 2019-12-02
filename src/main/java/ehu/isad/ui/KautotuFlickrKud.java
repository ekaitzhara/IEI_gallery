package ehu.isad.ui;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.people.User;
import com.flickr4java.flickr.util.AuthStore;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth1Token;
import ehu.isad.Main;
import ehu.isad.flickr.FlickrAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class KautotuFlickrKud implements Initializable {

    private Main mainApp;
    private AuthStore authStore;
    private String url;
    private OAuth1RequestToken requestToken;
    private AuthInterface authInterface;

    @FXML
    private Hyperlink esteka = new Hyperlink();

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private TextField txtKode = new TextField();

    public void setMainApp(Main main) {
        this.mainApp = main;
    }

    @FXML
    public void klikatuURL(ActionEvent actionEvent) {
        // Abrir navegador con la url
        System.out.println(esteka.getText());

        try {
            String osName = System.getProperty("os.name");
            if (osName.contains("Windows"))
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            else if (osName.contains("Linux"))
                Runtime.getRuntime().exec("xdg-open " + url);
            else if (osName.contains("Mac OS X"))
                Runtime.getRuntime().exec("open " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void kautotuHartuKodea(ActionEvent actionEvent) throws IOException, FlickrException {
        this.authStore = FlickrAPI.getInstantzia().getAuthStore();

        //AuthInterface authInterface = FlickrAPI.getInstantzia().getFlickr().getAuthInterface();
        //OAuth1RequestToken requestToken = authInterface.getRequestToken();

        String tokenKey = txtKode.getText();

        System.out.println(tokenKey);

        OAuth1Token accessToken = this.authInterface.getAccessToken(this.requestToken, tokenKey);

        Auth auth = this.authInterface.checkToken(accessToken);
        User erabiltzailea = auth.getUser();
        this.mainApp.jarriErabiltzaileID(erabiltzailea.getId());
        this.mainApp.jarriErabiltzaileIzena(erabiltzailea.getRealName());
        RequestContext.getRequestContext().setAuth(auth);
        this.authStore.store(auth);
        FlickrAPI.getInstantzia().setAuthStore(this.authStore);

        System.out.println("Thanks.  You probably will not have to do this every time.  Now starting backup.");

        this.mainApp.pantailaNagusiaErakutsi();
    }


    //This method is called upon fxml load
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FlickrAPI api = FlickrAPI.getInstantzia();
        try {

            this.authStore = api.getAuthStore();
            this.authInterface = api.getFlickr().getAuthInterface();
            this.requestToken = authInterface.getRequestToken();
            this.url = authInterface.getAuthorizationUrl(requestToken, Permission.WRITE); // hemen zehaztu baimenak
        } catch (Exception e){
            api.conectionError();
        }

    }
}
