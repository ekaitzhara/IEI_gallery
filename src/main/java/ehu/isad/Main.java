package ehu.isad;

import com.flickr4java.flickr.FlickrException;
import ehu.isad.controller.Kautoketa2Zatia;
import ehu.isad.controller.KautotuKud;
import ehu.isad.controller.MainKud;
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
  private Parent mainUI;
  private Parent kautotu2UI;

  private Stage stage;

  private KautotuKud kautotuKud;
  private MainKud mainKud;
  private Kautoketa2Zatia kautotu2zatia;


  @Override
  public void start(Stage primaryStage) throws Exception {

    stage = primaryStage;
    pantailakKargatu();

    stage.setTitle("Argazki Backup");
    stage.setScene(new Scene(kautotuUI, 450, 275));
    stage.show();
  }

  private void pantailakKargatu() throws IOException {

    Locale locale = new Locale("eu","ES");
    ResourceBundle bundle = ResourceBundle.getBundle("UIResources", locale);


    FXMLLoader loaderKautotu = new FXMLLoader(getClass().getResource("/kautotu.fxml"), bundle);
    kautotuUI = (Parent) loaderKautotu.load();
    kautotuKud = loaderKautotu.getController();
    kautotuKud.setMainApp(this);

    FXMLLoader loaderMain = new FXMLLoader(getClass().getResource("/main.fxml"), bundle);
    mainUI = (Parent) loaderMain.load();
    mainKud = loaderMain.getController();
    mainKud.setMainApp(this);

    FXMLLoader loaderKautotu2 = new FXMLLoader(getClass().getResource("/kautotu2.fxml"));
    kautotu2UI = (Parent) loaderKautotu2.load();
    kautotu2zatia = loaderKautotu2.getController();
    kautotu2zatia.setMainApp(this);
  }


  public static void main(String[] args) {
    launch(args);
  }

  public void mainErakutsi() {
    stage.setScene(new Scene(mainUI));
    stage.show();
  }

  public void kautotuURLGorde(String url) {
    kautotu2zatia.setUrl(url);
  }

  public void kautotu2zatiaerakutsi() {
    String u = null;
    try {
      u = kautotu2zatia.emanUrl();
    } catch (FlickrException e) {
      e.printStackTrace();
    }
    kautotuURLGorde(u);
    System.out.println(u);
    stage.setScene(new Scene(kautotu2UI));
    stage.show();
  }
}
