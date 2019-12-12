package ehu.isad.flickr;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import com.flickr4java.flickr.util.IOUtilities;
import ehu.isad.Main;
import ehu.isad.flickrKud.KautotuFlickrKud;
import ehu.isad.flickrKud.Laguntzaile;
import ehu.isad.model.*;
import ehu.isad.flickrKud.KautotuKud;

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

    private Boolean conection = true;

    private AuthStore authStore;

    // Singleton patroia
    private static FlickrAPI instantzia = new FlickrAPI();

    public static FlickrAPI getInstantzia() {
        if (instantzia == null)
            instantzia = new FlickrAPI();
        return instantzia;
    }

    private FlickrAPI() {
        Properties properties = null;
        InputStream in = null;
        try {
            in = FlickrAPI.class.getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtilities.close(in);
            // Setup properties hutsik dago
            // COMO PONGO LA NUEVA ESCENA DESDE AQUI?????

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

    public Boolean hasConection(){ return conection; }
    public void conectionError(){conection = false;}

    public void BildumaSortu(){

    }

    public String argazkiaIgo(String photoPath,String albumName){
        String titulua = Laguntzaile.getFileName(photoPath);
        Uploader up = flickr.getUploader();

        UploadMetaData umd = new UploadMetaData();
        // Sartu argazkiaren izena
        umd.setTitle(titulua);
        // Sartu deskribapena
        umd.setDescription("Tmp-tik igotako argazkia");

        // Sartu argazkia
        File pathToFile = new File(photoPath);
        String key = getFlickr().getApiKey();
        Auth auth =  getFlickr().getAuth();

        try {
            if(albumName.equals("NotInASet")){
                return up.upload(pathToFile, umd);
            }else{
                String id = up.upload(pathToFile, umd);
                flickr.getPhotosetsInterface().create(albumName,"",id);  //albun berria sortzen
                return id;
            }
        } catch (FlickrException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void argazkiaDeskargatu(String filename){

        String argazkiID = null; // Hemen erabiltzaileak nonbait sartuko du argazkiaren URL-a edo izena, eta guk bilatu beharko dugu eta ID-a lortu

        //PhotosetsInterface pi = flickr.getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = flickr.getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea
        URL url = null;
        try {
            Photo p = photoInt.getInfo(argazkiID, FlickrAPI.getInstantzia().getSecret());

            // Argazkia edukita, p.getOriginalUrl() -rekin argazkiaren helbidea lortuko dugu

            // Orain URL-tik argazkia irakurriko dugu

            Laguntzaile.downloadFileWithUrl(filename, p.getOriginalUrl());

        } catch (FlickrException e) {
            e.printStackTrace();
        }
    }


    public void argazkiaJaitsiEtaGorde(String filename, String argazkiUrl) {
        URL url = null;
        try {
            url = new URL(argazkiUrl);
        } catch (MalformedURLException e) { e.printStackTrace(); }

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
            FileOutputStream fos = new FileOutputStream("/data/dasiteam/flickr/argazkiak/"+filename+ ".jpg");
            fos.write(response);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Gure ordenagailuan argazki berri bat sortuko dugu
        //FileOutputStream fos = new FileOutputStream("C:\\Users\\anderdu\\Downloads\\a.jpg");

        String path = this.getClass().getResource("/data/dasiteam/flickr/argazkiak").getPath();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path + filename);
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        // eta deskargatu dugun argazkia sartuko dugu bertan
        try {
            fos.write(response);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(filename + " argazkia ondo jaitsi eta resources/data/dasiteam/flickr/argazkiak karpetan gorde egin da");
    }

    public void ezabatuFlickrInstantzia() {
        instantzia = null;
    }

}
