package ehu.isad;

import ehu.isad.ui.KautotuFlickrKud;
import ehu.isad.ui.KautotuKud;
import ehu.isad.ui.PantailaNagusiKud;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

  private Scene eKautoketa;
  private Scene eAccessTokenLortu;
  private Scene pantailaNagusia;

  private Parent kautotuUI;
  private Parent pantailaNagusiUI;
  private Parent kautotuFlickrUI;

  private Stage stage;

  private KautotuKud kautotuKud;
  private PantailaNagusiKud pantailaNagusiKud;
  private KautotuFlickrKud kautotuFlickrKud;

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

    FXMLLoader loaderKautotu2 = new FXMLLoader(getClass().getResource("/view/kautotuFlickr.fxml"), bundle);
    kautotuFlickrUI = (Parent) loaderKautotu2.load();
    kautotuFlickrKud = loaderKautotu2.getController();
    kautotuFlickrKud.setMainApp(this);
  }


  public static void main(String[] args) {
    launch(args);
  }

  public void pantailaNagusiaErakutsi() {
    stage.setScene(pantailaNagusia);
    stage.show();
  }

  public void kautoketaraEraman() {
    stage.setScene(eKautoketa);
    stage.show();
  }

  public void kautotu2zatiaerakutsi() {
    stage.setScene(eAccessTokenLortu);
    stage.show();
  }

  public void jarriErabiltzaileID(String id) {
    pantailaNagusiKud.gordeErabiltzaileID(id);
  }

  public void jarriErabiltzaileIzena(String izena) { pantailaNagusiKud.gordeErabiltzaileIzena(izena);
  }

  public void hizkuntzaAldatu(String hizkuntzBerria, String herrialdeBerria) throws Exception {
    hizkuntza = hizkuntzBerria;
    hizkuntzHerrialdea = herrialdeBerria;
    this.start(stage);
  }

  public void logoutAktibatu() {
    kautotuKud.logoutAktibatu();
  }
}
