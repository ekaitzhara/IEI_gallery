package ehu.isad.model;

import ehu.isad.Main;
import ehu.isad.flickrKud.ArgazkiaIgoKud;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;

import java.io.File;
import java.util.ArrayList;

public class ObsArgazkiIgo {
    private String izena;
    private Button botoia;
    private File archivo;

    public ObsArgazkiIgo(String izena, File pFile) {
        this.izena = izena;
        this.botoia = new Button();
        this.archivo = pFile;
    }

    public String getPath(){
        return archivo.getPath();
    }

    public String getIzena() {
        return izena;
    }

    public Button getBotoia() {
        return botoia;
    }

    public String idBotoi(){
        return botoia.getId();
    }

    public File getArchivo() {
        return archivo;
    }




}
