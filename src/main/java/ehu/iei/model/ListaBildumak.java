package ehu.iei.model;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.tags.Tag;
import ehu.isad.db.ArgazkiDBKud;
import ehu.isad.db.BildumaDBKud;
import ehu.isad.db.ErabiltzaileDBKud;
import ehu.isad.db.EtiketaDBKud;
import ehu.isad.flickr.FlickrAPI;
import ehu.isad.flickrKud.Utils;

import java.io.File;
import java.util.*;

public class ListaBildumak {
    //private

    private static ListaBildumak nireBilduma = null;
    private ArrayList<Bilduma> lista = null;

    private ListaBildumak(){
        lista = new ArrayList<Bilduma>();
    }

    public static ListaBildumak getNireBilduma() {
        if(nireBilduma==null){
            nireBilduma = new ListaBildumak();
        }
        return nireBilduma;
    }

    public void bildumaEzabatu(String bildumaIzen){
        Bilduma ezabatzekoB = this.emanBildumaIzenarekin(bildumaIzen);
        Bilduma notInASet = this.emanBildumaIzenarekin("NotInASet");
        for (Argazkia a : ezabatzekoB.getArgazkiak()) {
            notInASet.argazkiaGehitu(a);
        }
        this.lista.remove(ezabatzekoB);
        this.lista.remove(notInASet);
        this.lista.add(notInASet);

    }

    public Bilduma bildumaSartu(String bildumaIzena,String id){

        Bilduma berria = new Bilduma(bildumaIzena,id,null,null);
        lista.add(berria);
        return berria;
    }

    public void argazkiakSartu(ArrayList<Argazkia> argazkiLista, Bilduma non){
        for (Argazkia argazki : argazkiLista){
            non.argazkiaSartu(argazki);
        }
    }

    public List<Bilduma> lortuBildumak(){
        List<Bilduma> emaitza = new ArrayList<>();
        for(Bilduma bil:lista){
            emaitza.add(bil);
        }
        return emaitza;
    }

    public List<String> lortuBildumenIzenak() {
        List<String> emaitza = new ArrayList<>();
        for(Bilduma bil:lista){
            emaitza.add(bil.getIzena());
        }
        return emaitza;
    }

    public void listaBete() throws FlickrException {

        PhotosetsInterface pi = FlickrAPI.getInstantzia().getFlickr().getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
        PhotosInterface photoInt = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea

        Map<String, Collection> allPhotos = new HashMap<String, Collection>(); // sortu datu-egitura bat bildumak gordetzeko

        Iterator sets = pi.getList(FlickrAPI.getInstantzia().getNsid(), "description, views, date_upload, date_taken, tags").getPhotosets().iterator(); // nsid erabiltzailearen bildumak zeharkatzeko iteratzailea lortu

        Bilduma bildumaIzenarekin = null;
        String erab = ErabiltzaileDBKud.getIdErab();

        while (sets.hasNext()) { // bildumak dauden bitartean, zeharkatu
            Photoset set = (Photoset) sets.next(); // bilduma lortu
            if (this.emanBildumaIzenarekin(set.getTitle()) == null)
                lista.add(new Bilduma(set.getTitle(), set.getId(), set.getDescription(), erab)); // aqui sumamos las bildumas exisitentes, pero falta crear la bilduma de fotos sin bilduma
            // set es la bilduma
            Set<String> extras = new HashSet<>(Arrays.asList("description", "views", "date_upload", "date_taken", "tags"));

            PhotoList photos = pi.getPhotos(set.getId(), extras, 0, 500, 1);  // bildumaren lehenengo 500 argazki lortu
            allPhotos.put(set.getTitle(), photos);  // txertatu (bilduma --> bere argazkiak)
        }
        // Para encontrar lista de fotos sin bilduma
        int notInSetPage = 1;  // argazki batzuk bilduma batean sartu gabe egon daitezke...
        Collection notInASet = new ArrayList(); // horiek ere jaso nahi ditugu
        while (true) { // lortu bildumarik gabeko argazkiak, 50naka
            Collection nis = photoInt.getNotInSet(50, notInSetPage);
            notInASet.addAll(nis); //va cogiendo de 50 en 50 y hace add a "liburutegi" notInASet
            if (nis.size() < 50) {
                break;
            }
            notInSetPage++;
        }
        lista.add(new Bilduma("NotInASet", "0000", "", erab));
        allPhotos.put("NotInASet", notInASet); //  txertatu ( NotInASet --> bildumarik gabeko argazkiak)

        Iterator allIter = allPhotos.keySet().iterator(); // datu guztiak ditugu. bildumen izenak zeharkatzeko iteratzailea lortu

        while (allIter.hasNext()) {
            String setTitle = (String) allIter.next();  // bildumaren hurrengo izena lortu

            bildumaIzenarekin = this.emanBildumaIzenarekin(setTitle);

            System.out.println("Bilduma:" + setTitle);
            Collection currentSet = allPhotos.get(setTitle); // bildumaren argazkiak lortu
            Iterator setIterator = currentSet.iterator(); // bildumaren argazkiak zeharkatzeko iteratzailea lortu

            while (setIterator.hasNext()) { // bildumaren argazkiak zeharkatu

                Photo p = (Photo) setIterator.next();
                String pTitle = p.getTitle();
                String pDescription = p.getDescription();
                String pUrl = p.getSmallUrl();

                Date pDatePosted = p.getDatePosted();
                Date pDateTaken = p.getDateTaken();
                java.sql.Date dateSQL = null;
                if (pDateTaken != null) {
                    Long l = pDateTaken.getTime();
                    dateSQL = new java.sql.Date(l);
                } else if (pDatePosted!=null) {
                    Long l = pDatePosted.getTime();
                    dateSQL = new java.sql.Date(l);
                }

                String pId = p.getId();
                Boolean pFavourite = p.isFavorite();
                Integer favs = photoInt.getFavorites(pId, 50,1).size();
                Integer comments = p.getComments();
                Integer views = p.getViews();
                ArrayList<Etiketa> etiketenLista = new ArrayList<>();

                pTitle = pTitle.replace(" ","_");
                //String fitxategiIzena= pTitle + "." + p.getOriginalFormat();

                Collection<Tag> etiketak = p.getTags();
                for (Tag t : etiketak)
                    etiketenLista.add(new Etiketa(t.getValue()));


                bildumaIzenarekin.argazkiaGehitu(pTitle, pDescription , dateSQL, pId, pFavourite, erab, pUrl, favs, comments, etiketenLista, views);
                //aux.argazkiaGehitu(p.getTitle(), p.getDescription(), p.getMediumSize().toString(), (java.sql.Date) p.getDateAdded(), p.getId(), p.isFavorite(), erab);


                //FlickrAPI.getInstantzia().argazkiaJaitsiEtaGorde(pId, p.getSmallUrl());

                String fitxategiPath = Utils.globalPath("/data/dasiteam/flickr/argazkiak/") + pId + ".jpg";
                Utils.downloadFileWithUrl(p.getSmallUrl(), fitxategiPath);

                String title = p.getTitle();
                System.out.println("\t"+title);
            }
        }

    }

    public Bilduma emanBildumaIzenarekin(String titulua) {
        for(Bilduma bil:lista){
            if (bil.getIzena().equals(titulua))
                return bil;
        }
        return null;
    }

    public boolean listaHutsikDago() {
        return lista.isEmpty();
    }

    public ArrayList<Bilduma> getLista() {
        return lista;
    }

    public void argazkiaEzabatu(String ezabatzekoID) {
        for (Bilduma b : lista) {
            Argazkia a = b.bilatuArgazkiaIdFLickrrekin(ezabatzekoID);
            if (a != null)
                b.ezabatuArgazkia(a);
            if (b.getArgazkiak().isEmpty())
                    this.lista.remove(b);
        }
    }

    public List<TaulaDatu> emanTaularakoDatuak(String bilduma){
        List<TaulaDatu> emaitza = new ArrayList<>();
        // Sartu taularako behar diren datuak aukeratu duen bildumaren arabera
        ArrayList<Argazkia> argazkiak = emanArgazkiakBildumaIzenarekin(bilduma);
        for (Argazkia a : argazkiak) {
            String argazkiPath = Utils.argazkiakPath + File.separatorChar+ a.getIdFLickr() + ".jpg";
            String etiketak = a.emanStringEtiketak();
            TaulaDatu t = new TaulaDatu(a.getId(), argazkiPath, a.getIzena(), etiketak, a.getData(), a.getDeskribapena(), a.getFavs(), a.getKomentarioKop());
            emaitza.add(t);
        }
        return emaitza;
    }

    private ArrayList<Argazkia> emanArgazkiakBildumaIzenarekin(String bilduma) {
        for (Bilduma b : this.lista) {
            if (b.getIzena().equals(bilduma))
                return b.getArgazkiak();
        }
        return null;
    }

    public void sartuDatuakDBra() {
        for (Bilduma b : lista) {
            BildumaDBKud.getInstantzia().bildumaSartu(b.getIzena(), b.getId(), b.getSortzaileID(), b.getDeskribapena());
            ArrayList<Argazkia> argazkiak = b.getArgazkiak();
            for (Argazkia a : argazkiak) {
                ArgazkiDBKud.getInstantzia().argazkiaSartu(a.getId(), a.getIzena(), a.getDeskribapena(), a.getData(), a.getIdFLickr(), a.isGogokoaDa(), a.getSortzaileID(), a.getFavs(), a.getKomentarioKop());
                ArgazkiDBKud.getInstantzia().argazkiaBildumanSartu(a.getId(), b.getIzena());
                ArrayList<Etiketa> etiketak = a.getEtiketak();
                for (Etiketa e: etiketak) {
                    EtiketaDBKud.getInstantzia().etiketaSartu(e.getIdEtiketa(), e.getIzena());
                    EtiketaDBKud.getInstantzia().etiketaArgazkianSartu(e.getIdEtiketa(), a.getId());
                }
            }
        }
    }

    public void listaBeteDBrekin() {
        // ListaBildumak betetzen du DBko datu guztiekin
        this.lista =  BildumaDBKud.getInstantzia().emanListaBildumak();
        for (Bilduma b : lista) {
            b.argazkiListaSartu(ArgazkiDBKud.getInstantzia().emanArgazkiakBildumarekin(b.getIzena()));
            for (Argazkia a : b.getArgazkiak()) {
                a.etiketenListaSartu(EtiketaDBKud.getInstantzia().etiketakEman(a.getId()));
            }
        }
    }

    public boolean utsik(){
        if(getLista().size()==0){
            return true;
        }else
            return false;
    }

    public void listaHustu() {
        this.lista = null;
        nireBilduma = null;
    }

    public void argazkiaEditatu(String idFlickr, java.sql.Date data, String izena, String deskribapena, String[] etiketak) {
        for (Bilduma b : lista) {
            Argazkia a = b.bilatuArgazkiaIdFLickrrekin(idFlickr);
            if (a != null) {
                a.editatuArgazkia(data, izena, deskribapena, etiketak);
            }

        }
    }

    public Argazkia argazkiaBilatu(String idFLickrArgazki) {
        for (Bilduma b: this.lista) {
            Argazkia a = b.bilatuArgazkiaIdFLickrrekin(idFLickrArgazki);
            if (a != null) {
                return a;
            }
        }
        return null;
    }
}
