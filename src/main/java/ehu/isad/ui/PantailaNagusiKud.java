package ehu.isad.ui;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import ehu.isad.Main;
import ehu.isad.db.ArgazkiDBKud;
import ehu.isad.db.ErabiltzaileDBKud;
import ehu.isad.flickr.FlickrAPI;
import ehu.isad.model.Bilduma;
import ehu.isad.model.ListaBildumak;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

public class PantailaNagusiKud implements Initializable {

  // Reference to the main application.
  private Main mainApp;

  private static String erabiltzaileID = ErabiltzaileDBKud.getIdErab();


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
  public void updateApi(ActionEvent actionEvent){
      syncEgin();
      System.out.println("update click");
  }

  public void syncEgin() {

      // 1. zatia
      // /tmp karpetan dagoen igo Flickr-rera eta Flickr-retik hartu sortu duen id-a sartzeko datu basean (idFlickr)
      // small size jaisten du eta resources-en guztiekin batera jartzen ditu argazki horiek
      // amaitzerakoan tmp-en dagoen ezabatzen du

      syncTMPZatia();

      // 2. zatia
      // deletedRegister.txt fitxategian gure datubasean ezabatu ditugun, baina Flikcer-rera aldaketa igo ezin izan ditugun argazkiak daude
      // Beraz, txt horretan dauden argazkian ez daude  ez gure datu basean, ez gure datu egituran (ListaBildumak)
      // txt horretan dauden argazki guztiak Flikcr-retik ezabatu behar dira
      // txt-an dagoena ezabatu (Flickr-ren ondo ezabatuta daudenean argazki guztiak)


      // 3. zatia
      // Datubaseko id guztiak sartzen ditugu ArrayList batean
      // Gero, Flickr-ren argazkiak hartu eta banan banan konparatu id-ak -->
      //    --> Flickr-eko argazkia datubasean badago, ArrayList-etik ezabatzen du
      // Azkenean, Flickr-ren ez dauden argazkiak geratuko dira soilik ArrayList-ean
      // Argazki horiek (sobratzen direnak) datubasetik eta datu egituratik (ListaBildumak) ezabatzen ditugu




  }

    private void syncTMPZatia() {
        String tmpPath = this.getClass().getResource("/tmp").getPath();
//        System.out.println(tmpPath);

        File infoTXT = null;
        String argazkiIzena = null;
        String idArgazkiDB = null;

        File tmp = new File(tmpPath);
        if (tmp.isDirectory()) {
            // Lehenik eta behin, argazkien informazio guztia duen File-a hartu behar dugu
            String[] txt_rako = tmp.list();
            if (txt_rako.length != 0) {
                for (String x : txt_rako) {
                    String mota = x.split("\\.")[1];
                    if (mota.equals("txt"))
                      infoTXT = new File(tmpPath + "infoArgazkiak.txt");
                }
            }

            // Orain argazkien zatia landuko dugu
            String[] argazkiak = tmp.list();
            if (argazkiak.length != 0) {
                  for (String a : argazkiak) {
                      String path = tmpPath + a;
                      String titulua = a.split("\\.")[0];
                      String artxiboMota = a.split("\\.")[1];
                      if (!artxiboMota.equals("txt")) {
                          try {
                              // txt-an argazki bilatu behar da (ez daudelako ordenaturik)
                              Scanner s = new Scanner(infoTXT);
                              while(s.hasNextLine() && !a.equals(argazkiIzena)) {
                                  String line = s.nextLine();
                                  argazkiIzena = line.split(",")[0];
                                  idArgazkiDB = line.split(",")[1];
                              }
                          } catch (FileNotFoundException e) { e.printStackTrace(); }
  //                        System.out.println(a + " argazkiaren datuak");
  //                        System.out.println("Argazkiaren izena => " + argazkiIzena);
  //                        System.out.println("Argazkiaren id-a DBrako => " + idArgazkiDB);

  //                      System.out.println("Argazki titulua => " + titulua);
  //                      System.out.println("Path => " + path);

                          String sortuDenFlickrID = FlickrAPI.getInstantzia().argazkiaIgo(path, titulua);
                          //System.out.println("Flickrren sortu den id-a " + sortuDenFlickrID);

              // DATU BASEAN DAGOENEAN DESKOMENTATU!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                          //ArgazkiDBKud.getInstantzia().idFlickrSartu(sortuDenFlickrID, idArgazkiDB);

                          PhotosInterface photoInt = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface();
                          try {
                              Photo p = photoInt.getPhoto(sortuDenFlickrID);
                              //System.out.println(p.getSmallUrl());
                              FlickrAPI.getInstantzia().argazkiaJaitsiEtaGorde(a, p.getSmallUrl());
                          } catch (FlickrException e) { e.printStackTrace(); }
                      }
                  }
                  System.out.println("tmp-eko argazki guztiak igo dira eta resources eta DBn sartu dira");
                  File[] ezabatzeko = tmp.listFiles();
                  for (File f : ezabatzeko)
                      f.delete();
                  System.out.println("tmp karpetako argazki guztiak ezabatu dira");
            } else
                System.out.println("Ez dago ezer tmp karpetan");


        }
    }

    @FXML
  public void programaItxi(ActionEvent actionEvent){
      System.out.println("itxi click");
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {


  }


    public void hartuEtaGordeDatuakFlickr() throws FlickrException {
      ArrayList<Bilduma> bildumak = ListaBildumak.getNireBilduma().getLista();
        if(ListaBildumak.getNireBilduma().listaHutsikDago())
            ListaBildumak.getNireBilduma().listaBete(); // paras descargar las bildumas
        bildumak = ListaBildumak.getNireBilduma().getLista();
        System.out.println("ListaBildumak bete egin da");
        // Hartu datu guztiak singleton-tik eta sartu taulan
    }
}