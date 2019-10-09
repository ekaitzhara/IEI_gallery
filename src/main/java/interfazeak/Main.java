package interfazeak;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sample.fxml"));
        Parent root = (Parent) loader.load();

        // Give the controller access to the main app.
        Controller controller = loader.getController();
        controller.setMainApp(this);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 450, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
