package ehu.isad.ui;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import ehu.isad.Main;
import ehu.isad.db.ErabiltzaileDBKud;
import ehu.isad.flickr.FlickrAPI;
import ehu.isad.model.Bilduma;
import ehu.isad.model.ListaBildumak;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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


  public void gordeErabiltzaileIzena(String izena) { this.erabiltzaileIzena.setText(erabiltzaileID); }

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