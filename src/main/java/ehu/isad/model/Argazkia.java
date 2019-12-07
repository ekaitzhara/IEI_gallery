package ehu.isad.model;

import java.sql.Date;
import java.util.ArrayList;

public class Argazkia {

    private String izena;
    private String deskribapena;
    private Integer id;
    private Date data;
    private String idFLickr;
    private boolean gogokoaDa;
    private String sortzaileID;
    private String url;
    private Integer favs;
    private Integer komentarioKop;
    private ArrayList<Etiketa> etiketak;

    private static Integer autoinc = 0;

    public Argazkia(String izena, String deskribapena, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID, String pUrl, Integer pFavs, Integer pKomentario, ArrayList<Etiketa> etiketaLista) {
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.id = autoinc++;
        this.data = data;
        this.idFLickr = idFLickr;
        this.gogokoaDa = gogokoaDa;
        this.sortzaileID = sortzaileID;
        this.url = pUrl;
        this.favs = pFavs;
        this.komentarioKop = pKomentario;
        this.etiketak = etiketaLista;
    }

    public boolean idFlickrHauDu(String pIdFlickr) {
        return this.idFLickr.equals(pIdFlickr);
    }

    public String getIzena() {
        return izena;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    public Integer getId() {
        return id;
    }

    public Date getData() {
        return data;
    }

    public String getIdFLickr() {
        return idFLickr;
    }

    public boolean isGogokoaDa() {
        return gogokoaDa;
    }

    public String getSortzaileID() {
        return sortzaileID;
    }

    public String getUrl() {
        return url;
    }

    public Integer getFavs() {
        return favs;
    }

    public Integer getKomentarioKop() {
        return komentarioKop;
    }

    public ArrayList<Etiketa> getEtiketak() {
        return etiketak;
    }

    public static Integer getAutoinc() {
        return autoinc;
    }
}
