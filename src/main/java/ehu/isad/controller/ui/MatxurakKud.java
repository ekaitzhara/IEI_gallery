package ehu.isad.controller;

import ehu.isad.Main;
import ehu.isad.MainMatxurak;
import ehu.isad.model.Autobusa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class MatxurakKud implements Initializable {

    @FXML
    private ComboBox matxuratutakoa;

    @FXML
    private ComboBox ordezkoa;

    @FXML
    private DatePicker noiz;

    // Reference to the main application.
    private MainMatxurak mainApp;

    public void setMainApp(MainMatxurak main) {
        this.mainApp = mainApp;
    }

    @FXML
    public void onClick(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Autobusa> autobusak = new ArrayList<>();
        autobusak.add(new Autobusa("QWERTY", 60, null));
        autobusak.add(new Autobusa("ASDFGH", 20, null));
        autobusak.add(new Autobusa("ZXCVBN", 50, null));


        matxuratutakoa.getItems().addAll(autobusak);

    }

}