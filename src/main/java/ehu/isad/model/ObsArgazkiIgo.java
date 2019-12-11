package ehu.isad.model;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class ObsArgazkiIgo {
    private String izena;
    private Button botoia;
    private File archivo;

    public ObsArgazkiIgo(String izena, File pFile) {
        this.izena = izena;
        this.botoia = new Button("delete");
        this.archivo = pFile;
    }

    public String getPath(){
        return archivo.getPath();
    }
}
