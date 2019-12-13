package ehu.isad.model;

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

    public File getArchivo() {
        return archivo;
    }
}
