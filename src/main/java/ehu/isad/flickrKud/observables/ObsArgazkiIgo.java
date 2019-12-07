package ehu.isad.flickrKud.observables;

import java.awt.*;

public class ObsArgazkiIgo {
    private String izena;
    private Button botoia;

    public ObsArgazkiIgo(String izena) {
        this.izena = izena;
        this.botoia = new Button("delete");
    }
}
