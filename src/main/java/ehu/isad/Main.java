package ehu.isad;

import ehu.isad.controller.AccessTokenLortuKud;
import ehu.isad.controller.KautotuKud;
import ehu.isad.controller.PantailaNagusiKud;
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

  private Parent kautotuUI;
  private Parent pantailaNagusiUI;
  private Parent accessTokenLortuUI;

  private Stage stage;

  private KautotuKud kautotuKud;
  private PantailaNagusiKud pantailaNagusiKud;
  private AccessTokenLortuKud accessTokenLortuKud;

  private static String hizkuntza = "eu";
  private static String hizkuntzHerrialdea = "ES";


  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;
    pantailakKargatu();


    eKautoketa = new Scene(kautotuUI, 450, 275);
    eAccessTokenLortu = new Scene(accessTokenLortuUI);

    stage.setTitle("DASI APP Argazki Backup");
    stage.setScene(eKautoketa);
    stage.show();
  }

  private void pantailakKargatu() throws IOException {

    // Hemen aldatu ahal da hizkuntza
    Locale locale = new Locale(hizkuntza,hizkuntzHerrialdea);
    ResourceBundle bundle = ResourceBundle.getBundle("UIResources", locale);

    FXMLLoader loaderKautotu = new FXMLLoader(getClass().getResource("/kautotu.fxml"), bundle);
    kautotuUI = (Parent) loaderKautotu.load();
    kautotuKud = loaderKautotu.getController();
    kautotuKud.setMainApp(this);

    FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/pantailaNagusia.fxml"), bundle);
    pantailaNagusiUI = (Parent) loaderMain.load();
    pantailaNagusiKud = loaderMain.getController();
    pantailaNagusiKud.setMainApp(this);

    FXMLLoader loaderKautotu2 = new FXMLLoader(getClass().getResource("/kautotu2.fxml"), bundle);
    accessTokenLortuUI = (Parent) loaderKautotu2.load();
    accessTokenLortuKud = loaderKautotu2.getController();
    accessTokenLortuKud.setMainApp(this);
  }


  public static void main(String[] args) {
    launch(args);
  }

  public void pantailaNagusiaErakutsi() {
    stage.setScene(new Scene(pantailaNagusiUI));
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
}
