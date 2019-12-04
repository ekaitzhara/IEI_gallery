package ehu.isad.model;

import java.sql.Date;

public class Argazkia {

    private String izena;
    private String deskribapena;
    private Integer id;
    private Date data;
    private String idFLickr;
    private boolean gogokoaDa;
    private String sortzaileID;
    private String url;

    private static Integer autoinc = 0;

    public Argazkia(String izena, String deskribapena, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID, String pUrl) {
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.id = autoinc++;
        this.data = data;
        this.idFLickr = idFLickr;
        this.gogokoaDa = gogokoaDa;
        this.sortzaileID = sortzaileID;
        this.url = pUrl;
    }
}
