package ehu.isad;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller {

  // Reference to the main application.
  private Main mainApp;

  @FXML
  private TextField txtErabiltzaile;

  @FXML
  private TextField txtPasahitza;

  public void setMainApp(Main mainApp) {
    this.mainApp = mainApp;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) {
    System.out.println(txtErabiltzaile.getText() + ":" + txtPasahitza.getText());

  }
}
