package ehu.isad.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import com.flickr4java.flickr.util.IOUtilities;
import ehu.isad.ui.KautotuKud;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FlickrAPI {

    private final Flickr flickr;

    private final String nsid;
    private final String secret;
    private final String apiKey;

    private AuthStore authStore;

    // Singleton patroia
    private static FlickrAPI instantzia = new FlickrAPI();

    public static FlickrAPI getInstantzia() {
        return instantzia;
    }

    private FlickrAPI() {
        Properties properties = null;
        InputStream in = null;
        try {
            in = KautotuKud.class.getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtilities.close(in);
        }

        File authsDir = new File(System.getProperty("user.home") + File.separatorChar + ".flickrAuth");
        flickr = new com.flickr4java.flickr.Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), new REST());
        this.nsid = properties.getProperty("nsid");
        this.apiKey = properties.getProperty("apiKey");
        this.secret = properties.getProperty("secret");

        if (authsDir != null) {
            try {
                this.authStore = new FileAuthStore(authsDir);
            }catch (FlickrException e) {
                e.printStackTrace();
            }
        }
    }

    public Flickr getFlickr() {
        return flickr;
    }

    public String getNsid() {
        return nsid;
    }

    public AuthStore getAuthStore() {
        return authStore;
    }

    public String getSecret() {
        return secret;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setAuthStore(AuthStore authStore) {
        this.authStore = authStore;
    }
}
