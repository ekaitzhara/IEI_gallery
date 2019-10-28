package ehu.isad.controller;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.util.AuthStore;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth1Token;
import ehu.isad.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Kautoketa2Zatia {

    private Main mainApp;
    private FlickrSortu fs;
    private AuthStore authStore;
    private String url;

    @FXML
    private Label estekaLabel;

    @FXML
    private Hyperlink esteka = new Hyperlink();

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private TextField txtKode;


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
        fs = new FlickrSortu();
        this.authStore = fs.getAuthStore();

        AuthInterface authInterface = fs.getFlickr().getAuthInterface();
        OAuth1RequestToken requestToken = authInterface.getRequestToken();


        //String tokenKey = new Scanner(System.in).nextLine();

        String tokenKey = txtKode.getText();

        OAuth1Token accessToken = authInterface.getAccessToken(requestToken, tokenKey);

        Auth auth = authInterface.checkToken(accessToken);
        RequestContext.getRequestContext().setAuth(auth);
        this.authStore.store(auth);
        System.out.println("Thanks.  You probably will not have to do this every time.  Now starting backup.");
    }

    public void setUrl(String url) {
        //esteka.setText(url);
        estekaLabel.setText(url);
        this.url = url;
        esteka = new Hyperlink(url);
    }

    public String emanUrl() throws FlickrException {
        fs = new FlickrSortu();
        this.authStore = fs.getAuthStore();

        AuthInterface authInterface = fs.getFlickr().getAuthInterface();
        OAuth1RequestToken requestToken = authInterface.getRequestToken();

        return authInterface.getAuthorizationUrl(requestToken, Permission.WRITE); // hemen zehaztu baimenak
    }

    //This method is called upon fxml load
    public void initialize(URL location, ResourceBundle resources) throws FlickrException {
        /*
        this.fs = new FlickrSortu();
        this.authStore = fs.getAuthStore();

        AuthInterface authInterface = fs.getFlickr().getAuthInterface();
        OAuth1RequestToken requestToken = authInterface.getRequestToken();

        String url = authInterface.getAuthorizationUrl(requestToken, Permission.WRITE); // hemen zehaztu baimenak
        System.out.println(url);
        esteka.setText(url);
         */

        esteka.setText("Badoa?");
    }
}
