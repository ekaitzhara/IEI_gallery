package ehu.isad.model;

import java.sql.Date;

public class Argazkia {

    private String izena;
    private String deskribapena;
    private Integer id;
    private String size;
    private Date data;
    private String idFLickr;
    private boolean gogokoaDa;
    private String sortzaileID;

    private static Integer autoinc = 0;

    public Argazkia(String izena, String deskribapena, String size, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID) {
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.id = autoinc++;
        this.size = size;
        this.data = data;
        this.idFLickr = idFLickr;
        this.gogokoaDa = gogokoaDa;
        this.sortzaileID = sortzaileID;
    }
}
