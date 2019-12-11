package ehu.isad.model;

import java.sql.Date;
import java.util.ArrayList;

public class Bilduma {


    private String izena;
    private ArrayList<Argazkia> argazkiak = null;
    private String id;
    private String deskribapena;
    private String sortzaileID;

    public Bilduma(String izena, String id, String deskribapena, String sortzaileID) {
        this.izena = izena;
        this.id = id;
        this.deskribapena = deskribapena;
        this.sortzaileID = sortzaileID;
        argazkiak = new ArrayList<Argazkia>();
    }

    public void argazkiListaSartu(ArrayList<Argazkia> argazkiLista) {
        this.argazkiak = argazkiLista;
    }

    public void argazkiaGehitu(String izena, String deskribapena, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID, String pUrl, Integer favs, Integer comments, ArrayList<Etiketa> etiketaLista, Integer views) {
        argazkiak.add(new Argazkia(izena, deskribapena, data, idFLickr, gogokoaDa, sortzaileID, pUrl, favs, comments, etiketaLista, views));
    }
    public void argazkiaGehitu(Argazkia foto) {
        argazkiak.add(foto);
    }



    public void argazkiaSartu(Argazkia berria) {
        argazkiak.add(berria);
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

    public String getIzena() {
        return izena;
    }

    public ArrayList<Argazkia> getArgazkiak() {
        return argazkiak;
    }

    public String getId() {
        return id;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    public String getSortzaileID() {
        return sortzaileID;
    }

    public Argazkia bilatuArgazkiaIdFLickrrekin(String idFlickr) {
        for (Argazkia ar : argazkiak) {
            if (ar.idFlickrHauDu(idFlickr))
                return ar;
        }
        return null;
    }

    public void ezabatuArgazkia(Argazkia a) {
        this.argazkiak.remove(a);
    }
}
