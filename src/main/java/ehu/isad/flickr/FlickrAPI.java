package ehu.isad.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import com.flickr4java.flickr.util.IOUtilities;
import ehu.isad.model.*;
import ehu.isad.ui.KautotuKud;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
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

    public ArrayList<Bilduma> eguneratu(){
        ArrayList<Bilduma> bildumak = new ArrayList<Bilduma>();
        return bildumak;
    }

    public void BildumaSortu(){

    }
    public void ArgazkiaIgo(){
        Uploader up = FlickrAPI.getInstantzia().getFlickr().getUploader();

        UploadMetaData umd = new UploadMetaData();
        // Sartu argazkiaren izena
        umd.setTitle("Proba igo argazki");
        // Sartu deskribapena
        umd.setDescription("Funtzionatzen du?");

        // Sartu argazkia
        File pathToFile = new File("/home/ekaitzhara/Imágenes/San-Mames.jpg");

        try {
            Image argazki = ImageIO.read(pathToFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            up.upload(pathToFile, umd);
        } catch (FlickrException e) {
            e.printStackTrace();
        }

    }

    public void ArgazkiaDeskargatu(){

        String argazkiID = null; // Hemen erabiltzaileak nonbait sartuko du argazkiaren URL-a edo izena, eta guk bilatu beharko dugu eta ID-a lortu

        //PhotosetsInterface pi = FlickrAPI.getInstantzia().getFlickr().getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea



        URL url = null;
        try {
            Photo p = photoInt.getInfo(argazkiID, FlickrAPI.getInstantzia().getSecret());

            // Argazkia edukita, p.getOriginalUrl() -rekin argazkiaren helbidea lortuko dugu

            // Orain URL-tik argazkia irakurriko dugu

            url = new URL(p.getOriginalUrl());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FlickrException e) {
            e.printStackTrace();
        }

        byte[] response = null;
        try {
            InputStream in2 = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in2.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in2.close();
            response = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gure ordenagailuan argazki berri bat sortuko dugu
        //FileOutputStream fos = new FileOutputStream("C:\\Users\\anderdu\\Downloads\\a.jpg");
        String fileName = "a";
        String home = System.getProperty("user.home");
        char slash =  File.separatorChar;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(home + slash + "Downloads" +slash+ fileName + ".jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // eta deskargatu dugun argazkia sartuko dugu bertan
        try {
            fos.write(response);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}