package ehu.isad.controller;

import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photosets.PhotosetsInterface;
import com.flickr4java.flickr.uploader.UploadMetaData;
import com.flickr4java.flickr.uploader.Uploader;
import com.flickr4java.flickr.util.AuthStore;
import ehu.isad.Main;
import ehu.isad.controller.flickr.FlickrAPI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.spec.PSSParameterSpec;
import java.util.ResourceBundle;

public class PantailaNagusiKud implements Initializable {

  // Reference to the main application.
  private Main mainApp;

  private static String erabiltzaileID = null;

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

  // Argazki berria sartzeko botoia ematerakoan
  public void argazkiBerriaSortu() {

  }


  // Argazki bat Flickr-era igo
  public void argazkiaFlickrIgo() {
      Uploader up = FlickrAPI.getInstantzia().getFlickr().getUploader();

      UploadMetaData umd = new UploadMetaData();
      // Sartu argazkiaren izena
      umd.setTitle("Proba igo argazki");
      // Sartu deskribapena
      umd.setDescription("Funtzionatzen du?");

      // Sartu argazkia
      File pathToFile = new File("/home/ekaitzhara/Imágenes/San-Mames.jpg");

      try {
        Image argazki = ImageIO.read(pathToFile);
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        up.upload(pathToFile, umd);
      } catch (FlickrException e) {
        e.printStackTrace();
      }
  }


  // Argazki bat Flickr-etik jaitsi
  public void argazkiaJaitsi() {

      String argazkiID = null; // Hemen erabiltzaileak nonbait sartuko du argazkiaren URL-a edo izena, eta guk bilatu beharko dugu eta ID-a lortu

      //PhotosetsInterface pi = FlickrAPI.getInstantzia().getFlickr().getPhotosetsInterface(); // lortu bildumak kudeatzeko interfazea
      PhotosInterface photoInt = FlickrAPI.getInstantzia().getFlickr().getPhotosInterface(); // lortu argazkiak kudeatzeko interfazea



      URL url = null;
      try {
        Photo p = photoInt.getInfo(argazkiID, FlickrAPI.getInstantzia().getSecret());

        // Argazkia edukita, p.getOriginalUrl() -rekin argazkiaren helbidea lortuko dugu

        // Orain URL-tik argazkia irakurriko dugu

        url = new URL(p.getOriginalUrl());

      } catch (MalformedURLException e) {
        e.printStackTrace();
      } catch (FlickrException e) {
        e.printStackTrace();
      }

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
      } catch (IOException e) {
        e.printStackTrace();
      }

      // Gure ordenagailuan argazki berri bat sortuko dugu
      //FileOutputStream fos = new FileOutputStream("C:\\Users\\anderdu\\Downloads\\a.jpg");
      String fileName = "a";
      String home = System.getProperty("user.home");
      char slash =  File.separatorChar;
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(home + slash + "Downloads" +slash+ fileName + ".jpg");
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }

      // eta deskargatu dugun argazkia sartuko dugu bertan
      try {
        fos.write(response);
        fos.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
  }

  // Bilduma berri bat sortzeko
  public void bildumaBerriaSortu() {
      // Datu basean bilduma berri bat sortu bere izenarekin eta nahi dituen argazkiekin
  }

  public void gordeErabiltzaileID(String id) {
        erabiltzaileID = id;
    }

  public void gordeErabiltzaileIzena(String izena) { this.erabiltzaileIzena.setText("Kaixo: " + izena); }

  @Override
  public void initialize(URL location, ResourceBundle resources) {


  }


}