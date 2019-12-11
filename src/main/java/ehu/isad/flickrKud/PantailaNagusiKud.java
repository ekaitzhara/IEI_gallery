package ehu.isad.flickrKud;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.Photoset;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import ehu.isad.Main;
import ehu.isad.db.ArgazkiDBKud;
import ehu.isad.db.DBKudeatzaile;
import ehu.isad.db.ErabiltzaileDBKud;
import ehu.isad.flickr.FlickrAPI;
import ehu.isad.model.Bilduma;
import ehu.isad.model.ListaBildumak;
import ehu.isad.model.TaulaDatu;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.URL;
import java.sql.Date;
import java.util.*;

public class PantailaNagusiKud implements Initializable {

  // Reference to the main application.
  private Main mainApp;

  private static String erabiltzaileID = ErabiltzaileDBKud.getIdErab();

    //FXML-ko elementuak
    @FXML
    private TableView<TaulaDatu> tbData;

    @FXML
    private TableColumn<TaulaDatu, Image> argazkia;

    @FXML
    private TableColumn<TaulaDatu, String> izena;

    @FXML
    private TableColumn<TaulaDatu, String> etiketak;

    @FXML
    private TableColumn<TaulaDatu, Date> data;

    @FXML
    private TableColumn<TaulaDatu, Integer> views;

    @FXML
    private TableColumn<TaulaDatu, Integer> favs;

    @FXML
    private TableColumn<TaulaDatu, Integer> comments;

    @FXML
    private ListView bildumenLista = new ListView();

    // add your data here from any source
    private ObservableList<TaulaDatu> taulaModels = FXCollections.observableArrayList();
    private ObservableList bildumaModel = FXCollections.observableArrayList();


  @FXML
  private javafx.scene.control.Label erabiltzaileIzena = new javafx.scene.control.Label();

  public void setMainApp(Main main) {
    this.mainApp = main;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) {

  }

  @FXML
  public void onClickLogOut(ActionEvent actionEvent) {

      FlickrAPI.getInstantzia().ezabatuFlickrInstantzia();
      this.mainApp.logoutAktibatu();
      File authsDir = new File(System.getProperty("user.home") + File.separatorChar + ".flickrAuth");

      if (authsDir.isDirectory()) {
          if (authsDir.list().length == 0) {
              authsDir.delete();
              System.out.println("El directorio " + authsDir + " ha sido borrado correctamente");
          } else {
              for (String temp : authsDir.list()) {
                  File fileDelete = new File(authsDir, temp);
                  fileDelete.delete();
              }
              if (authsDir.list().length == 0) {
                  authsDir.delete();
                  System.out.println("El directorio " + authsDir + " ha sido borrado correctamente");
              }
          }
      }

      this.mainApp.kautoketaraEraman();
  }

  // Bilduma berri bat sortzeko
  public void bildumaBerriaSortu() {
      // Datu basean bilduma berri bat sortu bere izenarekin eta nahi dituen argazkiekin
      System.out.println("bilduma sortu");
      this.mainApp.bildumaSortuErakutsi();
  }

    // Argazki berria sartzeko botoia ematerakoan
    public void argazkiBerriaSortu() {
        this.mainApp.argazkiaIgoErakutsi();
    }


  public void jarriErabiltzaileIzena() {
      String izena = ErabiltzaileDBKud.getInstantzia().emanIzena();
      this.erabiltzaileIzena.setText(izena); }

  @FXML
  public void syncEgin(ActionEvent actionEvent) throws FileNotFoundException {
      System.out.println("sync empezado");

      // 1. zatia
      // /tmp karpetan dagoen igo Flickr-rera eta Flickr-retik hartu sortu duen id-a sartzeko datu basean (idFlickr)
      // small size jaisten du eta resources-en guztiekin batera jartzen ditu argazki horiek
      // amaitzerakoan tmp-en dagoen ezabatzen du
      try {
          tmpArgazkiakIgo();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      // 2. zatia
      // photosToDelete.txt fitxategian gure datubasean ezabatu ditugun, baina Flikcer-rera aldaketa igo ezin izan ditugun argazkiak daude
      // Beraz, txt horretan dauden argazkian ez daude  ez gure datu basean, ez gure datu egituran (ListaBildumak)
      // txt horretan dauden argazki guztiak Flikcr-retik ezabatu behar dira
      // txt-an dagoena ezabatu (Flickr-ren ondo ezabatuta daudenean argazki guztiak)
      try {
          ezabatuakKenduFlickerren();
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      // 3. zatia
      // Datubaseko id guztiak sartzen ditugu ArrayList batean
      // Gero, Flickr-ren argazkiak hartu eta banan banan konparatu id-ak -->
      //    --> Flickr-eko argazkia datubasean badago, ArrayList-etik ezabatzen du
      // Azkenean, Flickr-ren ez dauden argazkiak geratuko dira soilik ArrayList-ean
      // Argazki horiek (sobratzen direnak) datubasetik eta datu egituratik (ListaBildumak) ezabatzen ditugu
      try {
          ezabatuakKenduPrograman();
      } catch (Exception e) {
          e.printStackTrace();
      }

  }

  private void tmpArgazkiakIgo() throws FileNotFoundException {
        // tmp-n gordetako argazkiak flickerrera igoko dira
        String tmpPath = this.getClass().getResource("/data/dasiteam/flickr/tmp").getPath();
        String photosToUploadTxt = getClass().getResource("/data/dasiteam/flickr/photosToUpload.txt").getPath();
        File tmp = new File(tmpPath);
        File photosToUploadFile = new File(photosToUploadTxt);
        HashMap<String,String> mapUpload = Laguntzaile.getHashTableFromTxt(photosToUploadTxt);

        String albumName = null;
        String idArgazkiDB = null;

        if (tmp.isDirectory()) {
            String[] argazkiak = tmp.list(); //del archivo photos ToUpload ha conseguido una lista de fotos
            //TODO probar si funciona
            for (String a : argazkiak) {
                String titulua = Laguntzaile.getFileName(a); //fitxategiaren izena lortu
                idArgazkiDB = mapUpload.get(a); //fitxategiaren datu baseko id-a lortu
                DBKudeatzaile.getInstantzia().

                String sortuDenFlickrID = FlickrAPI.getInstantzia().argazkiaIgo(tmpPath+"/"+a,); //flick-era argazkia igo
                // DATU BASEAN DAGOENEAN DESKOMENTATU!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                ArgazkiDBKud.getInstantzia().idFlickrSartu(sortuDenFlickrID, idArgazkiDB);
                PhotosInterface photoInt = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
                try {// Argazkia flick-er era igoko da eta small-size-a deskargatuko da
                    Photo p = photoInt.getPhoto(sortuDenFlickrID);
                    Laguntzaile.downloadFileWithUrl(a, p.getSmallUrl());
                } catch (FlickrException e) { e.printStackTrace(); }
            }
                Laguntzaile.deleteAllFilesFromDir(tmpPath);
                Laguntzaile.clearFile(photosToUploadTxt);
            } else
                System.out.println("Errorea tmp karpeta irakurtzean");
    }

    private void ezabatuakKenduFlickerren() throws FileNotFoundException {
        String deletedRegister = this.getClass().getResource("/data/dasiteam/flickr/photosToDelete.txt").getPath();
        System.out.println("llamo a deleted register");
        PhotosInterface photoInt = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
        Scanner sArg = new Scanner(deletedRegister);
        while(sArg.hasNextLine()) {
            String argazkiarenID = sArg.nextLine();
            try {
                photoInt.delete(argazkiarenID);
            }
            catch (FlickrException e) { e.printStackTrace(); }
        }
        sArg.close();
        Laguntzaile.clearFile(deletedRegister);
    }

    private void ezabatuakKenduPrograman(){
        ArrayList<String> flickrIdDB = ArgazkiDBKud.getInstantzia().emanIdFlickrGuztiak();
        PhotosetsInterface bildumaInt = FlickrAPI.getInstantzia().getFlickr().getPhotosetsInterface();
        try {
            Iterator bildumak = bildumaInt.getList(FlickrAPI.getInstantzia().getNsid()).getPhotosets().iterator();
            while (bildumak.hasNext()) { // bildumak dauden bitartean, zeharkatu
                Photoset set = (Photoset) bildumak.next(); // bilduma lortu
                // set es la bilduma
                PhotoList photos = bildumaInt.getPhotos(set.getId(), 500, 1);  // bildumaren lehenengo 500 argazki lortu
                Iterator it = photos.iterator();
                while (it.hasNext()) {
                    Photo argazki = (Photo) it.next();
                    if (flickrIdDB.contains(argazki.getId()))
                        flickrIdDB.remove(argazki.getId());
                }
            }
        } catch (FlickrException e) { e.printStackTrace(); }

        // Puntu honetan DBan bai eta Flickerren ez dauden argazkiak daude
        for (String ezabatzekoID: flickrIdDB) {
            // DBtik ezabatu
            ArgazkiDBKud.getInstantzia().argazkiaEzabatu(ezabatzekoID);
            // ListaBildumatik ezabatu
            ListaBildumak.getNireBilduma().argazkiaEzabatu(ezabatzekoID);
        }
    }

    @FXML
  public void programaItxi(ActionEvent actionEvent){
      System.out.println("itxi click");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {

      izena.setCellValueFactory(new PropertyValueFactory<>("izena"));
      etiketak.setCellValueFactory(new PropertyValueFactory<>("etiketak"));
      data.setCellValueFactory(new PropertyValueFactory<>("data"));
      views.setCellValueFactory(new PropertyValueFactory<>("views"));
      favs.setCellValueFactory(new PropertyValueFactory<>("favs"));
      comments.setCellValueFactory(new PropertyValueFactory<>("comments"));

      argazkia.setCellValueFactory(new PropertyValueFactory<TaulaDatu, Image>("argazkia"));

      argazkia.setCellFactory(p -> new TableCell<>() {
          public void updateItem(Image image, boolean empty) {
              if (image != null && !empty){
                  final ImageView imageview = new ImageView();
                  imageview.setFitHeight(100);
                  imageview.setFitWidth(100);
                  imageview.setImage(image);
                  setGraphic(imageview);
                  setAlignment(Pos.CENTER);
                  // tbData.refresh();
              }else{
                  setGraphic(null);
                  setText(null);
              }
          };
      });

  }


    public void hartuEtaGordeDatuakFlickr() throws FlickrException {

        ArrayList<Bilduma> bildumak = ListaBildumak.getNireBilduma().getLista();
        if(ListaBildumak.getNireBilduma().listaHutsikDago())
            ListaBildumak.getNireBilduma().listaBete(); // paras descargar las bildumas
        //bildumak = ListaBildumak.getNireBilduma().getLista();
        System.out.println("ListaBildumak bete egin da");
        // Hartu datu guztiak singleton-tik eta sartu taulan


        /*
        PhotosInterface p = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
        Photo ph = p.getPhoto("49177576151");
            Collection<Tag> etiketak = ph.getTags();
        for (Tag t:etiketak)
                System.out.println("Etiketa value: " + t.getValue());
                System.out.println("Komentarioak "+ph.getComments());
        System.out.println("Media: "+ph.getMedia());
        System.out.println("GeoData "+ph.getGeoData());
                    System.out.println("Views " +ph.getViews());

        Collection<User> favs =p.getFavorites("49177576151", 50, 1);
        for (User u:favs) {
            System.out.println("Favoritoak " + u.toString());
        }
                    System.out.println("Fav count "+favs.size());
         */

    }

    public void sartuBildumakListan() {

      List<String> bildumenIzenak = ListaBildumak.getNireBilduma().lortuBildumenIzenak();
      bildumaModel = FXCollections.observableArrayList(bildumenIzenak);
      this.bildumenLista.setItems(bildumaModel);
      this.bildumenLista.getSelectionModel().selectFirst();

    }

    @FXML
    public void bildumanKlikatu(MouseEvent mouseEvent) {
      sartuDatuakTaulan();
    }

    public void sartuDatuakTaulan() {

      // Hasierako aldirako, jarri lehenengo bilduma aukeratu bezala
      //datu baseko datuak bildumetan sartu
      ListaBildumak datuEgitura = ListaBildumak.getNireBilduma();
      if(!datuEgitura.utsik()){
          String bilduma = (String) bildumenLista.getSelectionModel().getSelectedItem();
          //String bilduma = null; // bilduma = bildumenZerrendanAukeratua.getValue()
          this.taulaModels = FXCollections.observableArrayList(
                  ListaBildumak.getNireBilduma().emanTaularakoDatuak(bilduma)
          );
          this.tbData.setItems(taulaModels);
      }
    }


    public void taulaRefreshEgin() {
      tbData.refresh();
    }

}