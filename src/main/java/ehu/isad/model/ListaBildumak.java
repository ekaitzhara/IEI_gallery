package ehu.isad.model;

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

    public void bildumaEzabatu(Bilduma zein){
        lista.remove(zein);
    }

    public void bildumaSartu(String bildumaIzena){

        //Bilduma berria = new Bilduma(bildumaIzena);
        //lista.add(berria);
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

        Iterator sets = pi.getList(FlickrAPI.getInstantzia().getNsid(), "description, views, date_upload, tags").getPhotosets().iterator(); // nsid erabiltzailearen bildumak zeharkatzeko iteratzailea lortu

        Bilduma aux = null;
        String erab = ErabiltzaileDBKud.getIdErab();

        while (sets.hasNext()) { // bildumak dauden bitartean, zeharkatu
            Photoset set = (Photoset) sets.next(); // bilduma lortu
            lista.add(new Bilduma(set.getTitle(), set.getId(), set.getDescription(), erab)); // aqui sumamos las bildumas exisitentes, pero falta crear la bilduma de fotos sin bilduma
            // set es la bilduma
            Set<String> extras = new HashSet<>(Arrays.asList("description", "views", "date_upload", "tags"));

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

            aux = this.emanBildumaIzenarekin(setTitle);

            System.out.println("Bilduma:" + setTitle);
            Collection currentSet = allPhotos.get(setTitle); // bildumaren argazkiak lortu
            Iterator setIterator = currentSet.iterator(); // bildumaren argazkiak zeharkatzeko iteratzailea lortu

            while (setIterator.hasNext()) { // bildumaren argazkiak zeharkatu

                Photo p = (Photo) setIterator.next();
                String pTitle = p.getTitle();
                String pDescription = p.getDescription();
                String pUrl = p.getSmallUrl();


                Date pDate = p.getDateAdded();
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

                aux.argazkiaGehitu(pTitle, pDescription , (java.sql.Date) pDate,pId , pFavourite, erab, pUrl, favs, comments, etiketenLista, views);
                //aux.argazkiaGehitu(p.getTitle(), p.getDescription(), p.getMediumSize().toString(), (java.sql.Date) p.getDateAdded(), p.getId(), p.isFavorite(), erab);


                FlickrAPI.getInstantzia().argazkiaJaitsiEtaGorde(pId, p.getSmallUrl());

                String title = p.getTitle();
                System.out.println("\t"+title);
            }
        }

    }

    private Bilduma emanBildumaIzenarekin(String titulua) {
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
        }
    }

    public List<TaulaDatu> emanTaularakoDatuak(String bilduma) {
        List<TaulaDatu> emaitza = new ArrayList<>();
        // Sartu taularako behar diren datuak aukeratu duen bildumaren arabera
        ArrayList<Argazkia> argazkiak = emanArgazkiakBildumaIzenarekin(bilduma);
        for (Argazkia a : argazkiak) {
            String argazkiPath = this.getClass().getResource("/data/dasiteam/flickr/argazkiak").toString() + a.getIzena();
            String etiketak = a.emanStringEtiketak();
            TaulaDatu t = new TaulaDatu(a.getId(), argazkiPath, a.getIzena(), etiketak, a.getData(), a.getViews(), a.getFavs(), a.getKomentarioKop());
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
            BildumaDBKud.getInstantzia().bildumaSartu(b.getId(), b.getIzena(), b.getSortzaileID(), b.getDeskribapena());
            ArrayList<Argazkia> argazkiak = b.getArgazkiak();
            for (Argazkia a : argazkiak) {
                ArgazkiDBKud.getInstantzia().argazkiaSartu(a.getId(), a.getIzena(), a.getDeskribapena(), a.getData(), a.getIdFLickr(), a.isGogokoaDa(), a.getSortzaileID());
                ArgazkiDBKud.getInstantzia().argazkiaBildumanSartu(a.getId(), b.getId());
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
        if (BildumaDBKud.getInstantzia().bildumaHutsaDago())
            this.lista.add(new Bilduma("NotInASet", "0000", "", FlickrAPI.getInstantzia().getNsid()));
        for (Bilduma b : lista) {
            b.argazkiListaSartu(ArgazkiDBKud.getInstantzia().emanArgazkiakBildumarekin(b.getId()));
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
}
