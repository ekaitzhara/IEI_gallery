package ehu.isad.model;

import java.awt.*;
import java.io.File;

public class ObsArgazkiIgo {
    private String izena;
    private Button botoia;
    private File archivo;

    public ObsArgazkiIgo(String izena, File pFile) {
        this.izena = izena;
        this.botoia = new Button("delete");
        this.archivo = pFile;
    }
}
