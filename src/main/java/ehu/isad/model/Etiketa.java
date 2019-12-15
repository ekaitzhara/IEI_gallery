package ehu.isad.model;

import ehu.isad.flickrKud.Utils;

public class Etiketa {

    private Integer idEtiketa;
    private String izena;

    private static Integer autoInc = 0;

    public Etiketa(String izena) {
        this.idEtiketa = autoInc++;
        this.izena = izena;
    }

    public Etiketa(Integer idEtiketa, String izena) {
        this.idEtiketa = idEtiketa;
        this.izena = izena;
    }

    public Integer getIdEtiketa() {
        return idEtiketa;
    }

    public String getIzena() {
        return izena;
    }

    public static Integer getAutoInc() {
        return autoInc;
    }
}
