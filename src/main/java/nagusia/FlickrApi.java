
package nagusia;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.RequestContext;
import com.flickr4java.flickr.auth.Auth;
import com.flickr4java.flickr.auth.AuthInterface;
import com.flickr4java.flickr.auth.Permission;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.AuthStore;
import com.flickr4java.flickr.util.FileAuthStore;
import com.flickr4java.flickr.util.IOUtilities;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuth1Token;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class FlickrApi {

    // Hemen Flickr-en kautotzea eta metodoak jarriko ditugu
    // Argazki bat igo, jaitsi, bilduma berria sortu...
    private final String nsid;
    private static String apiKey;
    private static String secret;

    private final Flickr flickr;

    private AuthStore authStore;

    public FlickrApi() throws FlickrException {
        /**
         * Flickr-ekin komunikazioa kudeatzen du
         *
         */


        Properties properties = null;
        InputStream in = null;
        try {
            in = FlickrApi.class.getResourceAsStream("/setup.properties"); //Flickr apia hasiarazteko behar diren balioak
            System.out.println(in);
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
        this.apiKey = properties.getProperty("apiKey");
        this.secret = properties.getProperty("secret");

        if (authsDir != null) {
            this.authStore = new FileAuthStore(authsDir);
        }

    }

    private void authorize() throws IOException, FlickrException {
        AuthInterface authInterface = flickr.getAuthInterface();
        OAuth1RequestToken requestToken = authInterface.getRequestToken();

        String url = authInterface.getAuthorizationUrl(requestToken, Permission.WRITE); // hemen zehaztu baimenak
        System.out.println("Follow this URL to authorise yourself on Flickr");
        System.out.println(url);
        System.out.println("Paste in the token it gives you:");
        System.out.print(">>");

        String tokenKey = new Scanner(System.in).nextLine();

        OAuth1Token accessToken = authInterface.getAccessToken(requestToken, tokenKey);

        Auth auth = authInterface.checkToken(accessToken);
        RequestContext.getRequestContext().setAuth(auth);
        this.authStore.store(auth);
        System.out.println("Thanks.  You probably will not have to do this every time.  Now starting backup.");
    }

    public void dagoenGuztiaLortu() throws Exception {

        PhotosetsInterface pi = flickr.getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = flickr.getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea
        Map<String, Collection> allPhotos = new HashMap<String, Collection>(); // sortu datu-egitura bat bildumak gordetzeko

        Iterator sets = pi.getList(this.nsid).getPhotosets().iterator(); // nsid erabiltzailearen bildumak zeharkatzeko iteratzailea lortu

        while (sets.hasNext()) { // bildumak dauden bitartean, zeharkatu
            Photoset set = (Photoset) sets.next(); // bilduma lortu
            PhotoList photos = pi.getPhotos(set.getId(), 500, 1);  // bildumaren lehenengo 500 argazki lortu
            allPhotos.put(set.getTitle(), photos);  // txertatu (bilduma --> bere argazkiak)
        }

        int notInSetPage = 1;  // argazki batzuk bilduma batean sartu gabe egon daitezke...
        Collection notInASet = new ArrayList(); // horiek ere jaso nahi ditugu
        while (true) { // lortu bildumarik gabeko argazkiak, 50naka
            Collection nis = photoInt.getNotInSet(50, notInSetPage);
            notInASet.addAll(nis);
            if (nis.size() < 50) {
                break;
            }
            notInSetPage++;
        }
        allPhotos.put("NotInASet", notInASet); //  txertatu ( NotInASet --> bildumarik gabeko argazkiak)

        Iterator allIter = allPhotos.keySet().iterator(); // datu guztiak ditugu. bildumen izenak zeharkatzeko iteratzailea lortu

        while (allIter.hasNext()) {
            String setTitle = (String) allIter.next();  // bildumaren hurrengo izena lortu
            System.out.println("Bilduma:" + setTitle);
            Collection currentSet = allPhotos.get(setTitle); // bildumaren argazkiak lortu
            Iterator setIterator = currentSet.iterator(); // bildumaren argazkiak zeharkatzeko iteratzailea lortu

            while (setIterator.hasNext()) { // bildumaren argazkiak zeharkatu

                Photo p = (Photo) setIterator.next();
                String title = p.getTitle();
                System.out.println("\t"+title);
            }
        }
    }

    private void kautotu() throws IOException, FlickrException {

        RequestContext rc = RequestContext.getRequestContext();

        if (this.authStore != null) {
            Auth auth = this.authStore.retrieve(this.nsid);
            if (auth == null) {
                this.authorize(); // throws Exception
            } else {
                rc.setAuth(auth);
            }
        }
    }

    public ArrayList<String> bildumakLortu() throws Exception {
        ArrayList<String> bildumak = new ArrayList<String>();

        PhotosetsInterface pi = flickr.getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = flickr.getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea
        Map<String, Collection> allPhotos = new HashMap<String, Collection>(); // sortu datu-egitura bat bildumak gordetzeko

        Iterator sets = pi.getList(this.nsid).getPhotosets().iterator(); // nsid erabiltzailearen bildumak zeharkatzeko iteratzailea lortu

        while (sets.hasNext()) { // bildumak dauden bitartean, zeharkatu
            Photoset set = (Photoset) sets.next(); // bilduma lortu
            PhotoList photos = pi.getPhotos(set.getId(), 500, 1);  // bildumaren lehenengo 500 argazki lortu
            allPhotos.put(set.getTitle(), photos);  // txertatu (bilduma --> bere argazkiak)
        }

        Iterator allIter = allPhotos.keySet().iterator(); // datu guztiak ditugu. bildumen izenak zeharkatzeko iteratzailea lortu

        while (allIter.hasNext()) {
            String titulua = (String) allIter.next();  // bildumaren hurrengo izena lortu
            System.out.println(titulua);
            bildumak.add(titulua);
        }

        return bildumak;
    }


    // BILDUMA KLASEAREN METODOAK
    /*
    * Emendik aurrera dauden metodoak bilduma klaseak erabiltzeko sortu dira
    * */
    public void loginEgin() throws IOException, FlickrException {
        kautotu();
    }

    public void argazkiaJaitsi(String photoId) throws FlickrException, IOException {
        /*
        * Argazki bat jaitsiko du ematen diogun argazkiID-aren arabera
        * TODO Argazkia tamaina conkretu batean jaitsiko du
        * */
        PhotosetsInterface pi = flickr.getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = flickr.getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea
        //Map<String, Collection> allPhotos = new HashMap<String, Collection>(); // sortu datu-egitura bat bildumak gordetzeko

        Photo p = photoInt.getInfo(photoId, this.secret);
        // Argazkia edukita, p.getOriginalUrl() -rekin argazkiaren helbidea lortuko dugu

        // Orain URL-tik argazkia irakurriko dugu
        URL url = new URL(p.getOriginalUrl());
        InputStream in2 = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1!=(n=in2.read(buf)))
        {
            out.write(buf, 0, n);
        }
        out.close();
        in2.close();
        byte[] response = out.toByteArray();

        // Gure ordenagailuan argazki berri bat sortuko dugu
        //FileOutputStream fos = new FileOutputStream("C:\\Users\\anderdu\\Downloads\\a.jpg");
        String fileName = "a";
        String home = System.getProperty("user.home");
        char slash =  File.separatorChar;
        FileOutputStream fos = new FileOutputStream(home + slash + "Downloads" +slash+ fileName + ".jpg");
        // eta deskargatu dugun argazkia sartuko dugu bertan

        fos.write(response);
        fos.close();
    }
    public void bildumaJaitsi(){

    }


    public void argazkiaIgo() throws IOException, FlickrException {

        //PhotosetsInterface pi = flickr.getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        //PhotosInterface photoInt = flickr.getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea
        //Map<String, Collection> allPhotos = new HashMap<String, Collection>(); // sortu datu-egitura bat bildumak gordetzeko

        Uploader up = flickr.getUploader();

        UploadMetaData umd = new UploadMetaData();
        umd.setTitle("Proba igo argazki");
        umd.setDescription("Funtzionatzen du?");

        File pathToFile = new File("/home/ekaitzhara/Imágenes/San-Mames.jpg");
        FileInputStream input = new FileInputStream("/home/ekaitzhara/Imágenes/thumb-1920-707960.png");
        Image argazki = ImageIO.read(pathToFile);
        umd.getUploadParameters().put("perms","write");
        System.out.println(up.upload(pathToFile, umd));

        //ERROREA --> FlickrException: 99: Insufficient permissions. Method requires write privileges; read granted.
    }
    //flickr.photos.getInfo
    //public String getPhotoName(){}
    //public String getPhotoID(){}
    //public Boolean isPublicPhoto(){}
    //public int getPhotoYear(){}
    //flickr.photos.getSizes
    //openFileExplorer()



    public static void main(String[] args) throws Exception {
        FlickrApi fa = new FlickrApi();
        fa.kautotu();
        fa.argazkiaJaitsi("48742268508");
    }

}
