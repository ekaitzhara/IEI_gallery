package ehu.iei;

import com.flickr4java.flickr.FlickrException;
import ehu.iei.flickrKud.*;
import ehu.iei.flickrKud.error.SetPropErrorKud;
import ehu.iei.flickrKud.error.UploadErrorKud;
import ehu.iei.flickrKud.error.ZerbaitKlikaturikKud;
import ehu.iei.flickrKud.error.ZerbitzurikEzKud;
import ehu.iei.model.ListaBildumak;
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
  private Scene argazkiaIgo;
  private Scene bildumaSortu;
  private Scene uploadError;
  private Scene zerbaitKlikaturik;
  private Scene zerbitzurikEz;

  private Parent kautotuUI;
  private Parent pantailaNagusiUI;
  private Parent kautotuFlickrUI;
  private Parent argazkiaIgoUI;
  private Parent bildumaSortuUI;
  private Parent uploadErrorUI;
  private Parent setPropErrorUI;
  private Parent zerbaitKlikaturikUI;
  private Parent zerbitzurikEzUI;

  private Stage stage;

  private KautotuKud kautotuKud;
  private PantailaNagusiKud pantailaNagusiKud;
  private KautotuFlickrKud kautotuFlickrKud;
  private ArgazkiaIgoKud argazkiaIgoKud;
  private BildumaSortuKud bildumaSortuKud;
  private UploadErrorKud uploadErrorKud;
  private SetPropErrorKud setPropErrorKud;
  private ZerbaitKlikaturikKud zerbaitKlikaturikKud;
  private ZerbitzurikEzKud zerbitzurikEzKud;

  private static String hizkuntza = "eu";
  private static String hizkuntzHerrialdea = "ES";

  private ResourceBundle bundle;

  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;
    pantailakKargatu();


    eKautoketa = new Scene(kautotuUI, 500, 300);
    eAccessTokenLortu = new Scene(kautotuFlickrUI);
    pantailaNagusia = new Scene(pantailaNagusiUI);
    argazkiaIgo = new Scene(argazkiaIgoUI, 550, 500);
    bildumaSortu = new Scene(bildumaSortuUI, 450, 180);
    uploadError = new Scene(uploadErrorUI, 450, 450);
    zerbaitKlikaturik = new Scene(zerbaitKlikaturikUI, 400, 200);
    zerbitzurikEz = new Scene(zerbitzurikEzUI, 400, 250);

    stage.setTitle("DASI APP Photo Backup");
    stage.setScene(eKautoketa);
    stage.show();

    pantailanZentralizatu();
  }

  private void pantailakKargatu() throws IOException {

    // Hemen aldatu ahal da hizkuntza
    Locale locale = new Locale(hizkuntza,hizkuntzHerrialdea);
    bundle = ResourceBundle.getBundle("UIResources", locale);

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

    FXMLLoader loaderUploadError = new FXMLLoader(getClass().getResource("/view/error/uploadError.fxml"), bundle);
    uploadErrorUI = (Parent) loaderUploadError.load();
    uploadErrorKud = loaderUploadError.getController();
    uploadErrorKud.setMainApp(this);

    FXMLLoader loaderSetPropErrorKud = new FXMLLoader(getClass().getResource("/view/error/setPropError.fxml"), bundle);
    setPropErrorUI = (Parent) loaderSetPropErrorKud.load();
    setPropErrorKud = loaderSetPropErrorKud.getController();
    setPropErrorKud.setMainApp(this);

    FXMLLoader loaderZerbaitKlikaturikKud = new FXMLLoader(getClass().getResource("/view/error/zerbaitKlikaturik.fxml"), bundle);
    zerbaitKlikaturikUI = (Parent) loaderZerbaitKlikaturikKud.load();
    zerbaitKlikaturikKud = loaderZerbaitKlikaturikKud.getController();
    zerbaitKlikaturikKud.setMainApp(this);

    zerbaitKlikaturikKud.jarriBundle(bundle);

    FXMLLoader loaderZerbitzurikEzKud = new FXMLLoader(getClass().getResource("/view/error/zerbitzurikEz.fxml"), bundle);
    zerbitzurikEzUI = (Parent) loaderZerbitzurikEzKud.load();
    zerbitzurikEzKud = loaderZerbitzurikEzKud.getController();
    zerbitzurikEzKud.setMainApp(this);

  }


  public static void main(String[] args) {
    launch(args);
  }

  public void pantailaNagusiaErakutsi() {
    if (!Utils.emanSetupPropStatus()) {
      stage.setTitle(bundle.getString("DasiAPP Main Page"));
      pantailaNagusiKud.jarriErabiltzaileIzena();
      ListaBildumak.getNireBilduma().listaBeteDBrekin();
      //pantailaNagusiKud.syncEgin();
      pantailaNagusiKud.sartuBildumakListan();
      pantailaNagusiKud.sartuDatuakTaulan();
      stage.setScene(pantailaNagusia);
      stage.show();
      stage.setResizable(true);

      pantailanZentralizatu();
    } else
      setupPropertiesError();
  }

  public void kautoketaraEraman() {
    stage.setTitle(bundle.getString("kautoketa"));
    stage.setScene(eKautoketa);
    stage.show();

    pantailanZentralizatu();
  }

  public void kautotuFlickrErakutsi(String zerbitzua) {
    if (!Utils.emanSetupPropStatus()) {
      kautotuFlickrKud.gordeURL();
      kautotuFlickrKud.gordeZerbitzua(zerbitzua);
      stage.setScene(eAccessTokenLortu);
      stage.show();

      pantailanZentralizatu();
    } else
      setupPropertiesError();
  }

  public void bildumaSortuErakutsi(){

    stage.setTitle(bundle.getString("Bilduma sortu"));
    stage.setScene(bildumaSortu);
    stage.show();
    stage.setResizable(false);

    pantailanZentralizatu();
  }

  public void argazkiaIgoErakutsi(){
    argazkiaIgoKud.bildumakComboboxKargatu();
    stage.setTitle(bundle.getString("Argazkia igo"));
    stage.setScene(argazkiaIgo);
    stage.show();

    pantailanZentralizatu();
  }

  public void erroreaBistaratu(String erroreMota){
    if(erroreMota.equals("UploadError")) {
      stage.setTitle(bundle.getString("Upload Error"));
      stage.setScene(uploadError);
      stage.show();
    }
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
    pantailaNagusiKud.sartuBildumakListan();
    pantailaNagusiKud.sartuDatuakTaulan();

    stage.setScene(pantailaNagusia);
    stage.show();

    pantailanZentralizatu();
  }

  public void setupPropertiesError() {
    stage.setScene(new Scene(setPropErrorUI, 400, 210));
    stage.show();

    pantailanZentralizatu();
    stage.setResizable(false);
  }

  public void itxi() {
    stage.close();
  }

  public void zerbaitKlikaturikPantaila(){
    Stage secondStage = new Stage();
    secondStage.setTitle("DasiAPP Photo Backup");
    secondStage.setScene(zerbaitKlikaturik);
    secondStage.show();
    secondStage.setAlwaysOnTop(true);
    secondStage.setResizable(false);
  }

  public void zerbaitEditaturikPantaila() {
    Stage secondStage = new Stage();
    secondStage.setTitle("DasiAPP Photo Backup");
    zerbaitKlikaturikKud.jarriEditaturikTestua(hizkuntza, hizkuntzHerrialdea);
    secondStage.setScene(zerbaitKlikaturik);
    secondStage.show();
    secondStage.setAlwaysOnTop(true);
    secondStage.setResizable(false);
  }

  public void bildumaEzabatuError() {
    Stage secondStage = new Stage();
    secondStage.setTitle(bundle.getString("bildumaerror"));
    zerbaitKlikaturikKud.jarriBildumaErrorTestua(hizkuntza, hizkuntzHerrialdea);
    secondStage.setScene(zerbaitKlikaturik);
    secondStage.show();
    secondStage.setAlwaysOnTop(true);
    secondStage.setResizable(false);
  }

  public void syncEginMezua() {
    Stage secondStage = new Stage();
    secondStage.setTitle(bundle.getString("checkUpdates"));
    zerbaitKlikaturikKud.syncEginTestua(hizkuntza, hizkuntzHerrialdea);
    secondStage.setScene(zerbaitKlikaturik);
    secondStage.show();
    secondStage.setAlwaysOnTop(true);
    secondStage.setResizable(false);
  }

  public void zerbitzurikEz() {
    stage.setScene(zerbitzurikEz);
    stage.show();

    pantailanZentralizatu();
    stage.setResizable(false);
  }

  public void argazkiaEditatuError() {
    Stage secondStage = new Stage();
    secondStage.setTitle("DasiAPP Argazki Backup");
    zerbaitKlikaturikKud.jarriArgazkiEditatuErrorTestua(hizkuntza, hizkuntzHerrialdea);
    secondStage.setScene(zerbaitKlikaturik);
    secondStage.setWidth(500);
    secondStage.show();
    secondStage.setAlwaysOnTop(true);
    secondStage.setResizable(false);
  }

  public void checkUpdates() {
    Stage secondStage = new Stage();
    String aux = bundle.getString("checkUpdates");
    aux = aux.replace("...", "");
    secondStage.setTitle(aux);
    zerbaitKlikaturikKud.jarriEguneraketakBegiratuTestua(hizkuntza, hizkuntzHerrialdea);
    secondStage.setScene(zerbaitKlikaturik);
    secondStage.setWidth(500);
    secondStage.show();
    secondStage.setAlwaysOnTop(true);
    secondStage.setResizable(false);
  }

  public void aboutPantaila() {
    Stage secondStage = new Stage();
    secondStage.setTitle(bundle.getString("about"));
    zerbaitKlikaturikKud.jarriAboutTestua(hizkuntza, hizkuntzHerrialdea);
    secondStage.setScene(zerbaitKlikaturik);
    secondStage.setWidth(550);
    secondStage.show();
    secondStage.setAlwaysOnTop(true);
    secondStage.setResizable(false);
  }

  public void bildumaSortuError() {
    Stage secondStage = new Stage();
    secondStage.setTitle(bundle.getString("error"));
    zerbaitKlikaturikKud.jarriBildumaSortuErrorTestua(hizkuntza, hizkuntzHerrialdea);
    secondStage.setScene(zerbaitKlikaturik);
    secondStage.setWidth(550);
    secondStage.show();
    secondStage.setAlwaysOnTop(true);
    secondStage.setResizable(false);
  }

  private void pantailanZentralizatu() {
    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
    stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
    stage.setY((screenBounds.getHeight() - stage.getHeight()) / 2);
  }
}
