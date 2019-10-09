package interfazeak;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Controller {

  // Reference to the main application.
  private Main mainApp;

  @FXML
  private TextField txtErabiltzaile;

  @FXML
  private TextField txtPasahitza;

  public void setMainApp(Main main) {
    this.mainApp = mainApp;
  }

  @FXML
  public void onClick(ActionEvent actionEvent) {
    System.out.println(txtErabiltzaile.getText() + ":" + txtPasahitza.getText());
  }
}
