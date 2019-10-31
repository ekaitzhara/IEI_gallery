package nagusia;

import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.PhotosetsInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Bilduma {


    private String izena;
    private ArrayList<Argazkia> argazkiak;
    private Integer id;

    private static Integer autoinc;

    public Bilduma(String izena, ArrayList<Argazkia> argazkiak) {
        this.izena = izena;
        this.argazkiak = argazkiak;
        this.id = autoinc++;
    }

    public void argazkiaIgo() {

    }

    /*
    public void argazkiaJaitsi() throws FlickrException {
        PhotosetsInterface pi = flickr.getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = flickr.getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea
        Map<String, Collection> allPhotos = new HashMap<String, Collection>(); // sortu datu-egitura bat bildumak gordetzeko

        Photo p = photoInt.getInfo("48742475908", "3d7748707b36c7874b1fde67dc184804");
        System.out.println(p.toString());
    }


    public static void main(String[] args) throws FlickrException, IOException {
        FlickrApi fp = new FlickrApi();
        Bilduma b = new Bilduma("Bilduma1", new ArrayList<>());
        fp.loginEgin();
        b.argazkiaJaitsi();

    }

    */
}
