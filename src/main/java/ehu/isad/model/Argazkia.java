package ehu.isad.model;

public class Argazkia {

    private String izena;
    private String path;
    private Integer id;

    private static Integer autoinc;

    public Argazkia(String izena, String path) {
        this.izena = izena;
        this.path = path;
        this.id = autoinc++;
    }


}
