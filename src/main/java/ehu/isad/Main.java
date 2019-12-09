package ehu.isad;

import com.flickr4java.flickr.FlickrException;
import ehu.isad.flickrKud.*;
import ehu.isad.model.ListaBildumak;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

  public String username;
  public String usingApi;

  private Scene eKautoketa;
  private Scene eAccessTokenLortu;
  private Scene pantailaNagusia;

  private Parent kautotuUI;
  private Parent pantailaNagusiUI;
  private Parent kautotuFlickrUI;
  private Parent argazkiaIgoUI;
  private Parent bildumaSortuUI;

  private Stage stage;

  private KautotuKud kautotuKud;
  private PantailaNagusiKud pantailaNagusiKud;
  private KautotuFlickrKud kautotuFlickrKud;
  private ArgazkiaIgoKud argazkiaIgoKud;
  private BildumaSortuKud bildumaSortuKud;

  private static String hizkuntza = "eu";
  private static String hizkuntzHerrialdea = "ES";

  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;
    pantailakKargatu();


    eKautoketa = new Scene(kautotuUI, 500, 300);
    eAccessTokenLortu = new Scene(kautotuFlickrUI);
    pantailaNagusia = new Scene(pantailaNagusiUI);

    stage.setTitle("DASI APP Argazki Backup");
    stage.setScene(eKautoketa);
    stage.show();
  }

  private void pantailakKargatu() throws IOException {

    // Hemen aldatu ahal da hizkuntza
    Locale locale = new Locale(hizkuntza,hizkuntzHerrialdea);
    ResourceBundle bundle = ResourceBundle.getBundle("UIResources", locale);

    FXMLLoader loaderKautotu = new FXMLLoader(getClass().getResource("/view/kautotu.fxml"), bundle);
    kautotuUI = (Parent) loaderKautotu.load();
    kautotuKud = loaderKautotu.getController();
    kautotuKud.setMainApp(this);

    FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/view/pantailaNagusia.fxml"), bundle);
    pantailaNagusiUI = (Parent) loaderMain.load();
    pantailaNagusiKud = loaderMain.getController();
    pantailaNagusiKud.setMainApp(this);

    FXMLLoader loaderKautotuFlickr = new FXMLLoader(getClass().getResource("/view/kautotuFlickr.fxml"), bundle);
    kautotuFlickrUI = (Parent) loaderKautotuFlickr.load();
    kautotuFlickrKud = loaderKautotuFlickr.getController();
    kautotuFlickrKud.setMainApp(this);

    FXMLLoader loaderArgazkiaIgo = new FXMLLoader(getClass().getResource("/view/argazkiaIgo.fxml"), bundle);
    argazkiaIgoUI = (Parent) loaderArgazkiaIgo.load();
    argazkiaIgoKud = loaderArgazkiaIgo.getController();
    argazkiaIgoKud.setMainApp(this);

    FXMLLoader loaderBildumaSortu = new FXMLLoader(getClass().getResource("/view/bildumaSortu.fxml"), bundle);
    bildumaSortuUI = (Parent) loaderBildumaSortu.load();
    bildumaSortuKud = loaderBildumaSortu.getController();
    bildumaSortuKud.setMainApp(this);

  }


  public static void main(String[] args) {
    launch(args);
  }

  public void pantailaNagusiaErakutsi() {
    pantailaNagusiKud.jarriErabiltzaileIzena();
    ListaBildumak.getNireBilduma().listaBeteDBrekin();
    //pantailaNagusiKud.syncEgin();
    pantailaNagusiKud.sartuBildumakListan();
    pantailaNagusiKud.sartuDatuakTaulan();
    stage.setScene(pantailaNagusia);
    stage.show();

    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);

  }

  public void kautoketaraEraman() {
    stage.setScene(eKautoketa);
    stage.show();
  }

  public void kautotuFlickrErakutsi(String zerbitzua) {
    kautotuFlickrKud.gordeZerbitzua(zerbitzua);
    stage.setScene(eAccessTokenLortu);
    stage.show();
  }

  public void bildumaSortuErakutsi(){

    Stage stageLag = new Stage();
    stageLag.setTitle("Bilduma sortu");
    stageLag.setScene(new Scene(bildumaSortuUI, 450, 450));
    stageLag.show();
  }

  public void argazkiaIgoErakutsi(){
    Stage stageLag = new Stage();
    stageLag.setTitle("Argazkia igo");
    stageLag.setScene(new Scene(argazkiaIgoUI, 450, 450));
    stageLag.show();
  }

  public void erroreaBistaratu(String erroreMota) throws IOException {
    Stage stageLag = new Stage();
    if(erroreMota.equals("UploadError")) {
      FXMLLoader uploadError = new FXMLLoader(getClass().getResource("/view/bildumaSortu.fxml"));
      Parent uploadErrorUI = (Parent) uploadError.load();
      stageLag.setTitle("Upload Error");
      stageLag.setScene(new Scene(uploadErrorUI, 450, 450));
    }
    stageLag.show();
  }

  public void hizkuntzaAldatu(String hizkuntzBerria, String herrialdeBerria) throws Exception {
    hizkuntza = hizkuntzBerria;
    hizkuntzHerrialdea = herrialdeBerria;
    this.start(stage);
  }

  public void logoutAktibatu() {
    kautotuKud.logoutAktibatu();
  }

  public void syncEginLehenAldia() {
    pantailaNagusiKud.jarriErabiltzaileIzena();
    try {
      pantailaNagusiKud.hartuEtaGordeDatuakFlickr();
    } catch (FlickrException e) {
      e.printStackTrace();
    }
    // ListaBildumako datuak DBra sartu, lehen aldia delako
    ListaBildumak.getNireBilduma().sartuDatuakDBra();
  }
}
