package ehu.isad.model;

import java.util.Date;

public class Autobusa {

    private String matrikula;
    private int edukiera;
    private Date noizkoa;

    public Autobusa(String matrikula, int edukiera, Date noizkoa) {
        this.matrikula = matrikula;
        this.edukiera = edukiera;
        this.noizkoa = noizkoa;
    }

    @Override
    public String toString() {
        return matrikula;
    }
}
