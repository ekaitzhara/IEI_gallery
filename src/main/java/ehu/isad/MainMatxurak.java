package ehu.isad;

import ehu.isad.controller.Kautoketa2Zatia;
import ehu.isad.controller.KautotuKud;
import ehu.isad.controller.MainKud;
import ehu.isad.controller.MatxurakKud;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainMatxurak extends Application {

    private Parent autobusakUI;

    private Stage stage;

    private MatxurakKud matxurakKud;


    @Override
    public void start(Stage primaryStage) throws Exception {

        stage = primaryStage;
        pantailakKargatu();

        stage.setTitle("Argazki Backup");
        stage.setScene(new Scene(autobusakUI, 450, 275));
        stage.show();
    }

    private void pantailakKargatu() throws IOException {

        Locale locale = new Locale("eu","ES");
        ResourceBundle bundle = ResourceBundle.getBundle("UIResources", locale);


        FXMLLoader loaderKautotu = new FXMLLoader(getClass().getResource("/matxurak.fxml"), bundle);
        autobusakUI = (Parent) loaderKautotu.load();
        matxurakKud = loaderKautotu.getController();
        matxurakKud.setMainApp(this);

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void mainErakutsi() {
        stage.setScene(new Scene(autobusakUI));
        stage.show();
    }

    public void kautotu2zatiaerakutsi() {
        stage.setScene(new Scene(autobusakUI));
        stage.show();
    }
}
