package ehu.isad.controller;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import com.flickr4java.flickr.util.IOUtilities;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class FlickrSortu {

    private final Flickr flickr;

    private final String nsid;

    private AuthStore authStore;

    public FlickrSortu() throws FlickrException {
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
        flickr = new Flickr(properties.getProperty("apiKey"), properties.getProperty("secret"), new REST());
        this.nsid = properties.getProperty("nsid");

        if (authsDir != null) {
            this.authStore = new FileAuthStore(authsDir);
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
}
