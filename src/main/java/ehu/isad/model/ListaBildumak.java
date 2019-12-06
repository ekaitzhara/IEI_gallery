package ehu.isad.model;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.Size;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import ehu.isad.db.ErabiltzaileDBKud;
import ehu.isad.flickr.FlickrAPI;

import java.util.*;

public class ListaBildumak {
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

        Iterator sets = pi.getList(FlickrAPI.getInstantzia().getNsid()).getPhotosets().iterator(); // nsid erabiltzailearen bildumak zeharkatzeko iteratzailea lortu

        Bilduma aux = null;
        String erab = ErabiltzaileDBKud.getIdErab();

        while (sets.hasNext()) { // bildumak dauden bitartean, zeharkatu
            Photoset set = (Photoset) sets.next(); // bilduma lortu
            lista.add(new Bilduma(set.getTitle(), set.getId(), set.getDescription(), erab)); // aqui sumamos las bildumas exisitentes, pero falta crear la bilduma de fotos sin bilduma
            // set es la bilduma
            PhotoList photos = pi.getPhotos(set.getId(), 500, 1);  // bildumaren lehenengo 500 argazki lortu
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


                aux.argazkiaGehitu(pTitle, pDescription , (java.sql.Date) pDate,pId , pFavourite, erab, pUrl);
                //aux.argazkiaGehitu(p.getTitle(), p.getDescription(), p.getMediumSize().toString(), (java.sql.Date) p.getDateAdded(), p.getId(), p.isFavorite(), erab);

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
}
