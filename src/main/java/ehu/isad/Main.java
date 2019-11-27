package ehu.isad;

import ehu.isad.controller.Kautoketa2Zatia;
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

  private Parent kautotuUI;
  private Parent pantailaNagusiUI;
  private Parent kautotu2UI;

  private Stage stage;

  private KautotuKud kautotuKud;
  private PantailaNagusiKud pantailaNagusiKud;
  private Kautoketa2Zatia kautotu2zatia;

  private static String hizkuntza = "eu";
  private static String hizkuntzHerrialdea = "ES";


  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;
    pantailakKargatu();

    stage.setTitle("Argazki Backup");
    stage.setScene(new Scene(kautotuUI, 450, 275));
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
    kautotu2UI = (Parent) loaderKautotu2.load();
    kautotu2zatia = loaderKautotu2.getController();
    kautotu2zatia.setMainApp(this);
  }


  public static void main(String[] args) {
    launch(args);
  }

  public void pantailaNagusiaErakutsi() {
    stage.setScene(new Scene(pantailaNagusiUI));
    stage.show();
  }

  public void kautoketaraEraman() {
    stage.setScene(new Scene(kautotuUI));
    stage.show();
  }

  public void kautotu2zatiaerakutsi() {
    stage.setScene(new Scene(kautotu2UI));
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
