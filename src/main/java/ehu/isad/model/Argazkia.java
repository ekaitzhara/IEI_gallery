package ehu.isad.model;

import ehu.isad.db.EtiketaDBKud;
import ehu.isad.flickrKud.Utils;

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
    private Integer views;
    private ArrayList<Etiketa> etiketak;

    private static Integer autoinc = 0;

    public Argazkia(String izena, String deskribapena, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID, String pUrl, Integer pFavs, Integer pKomentario, ArrayList<Etiketa> etiketaLista, Integer pViews) {
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.id = Utils.argazkiInc--;
        this.data = data;
        this.idFLickr = idFLickr;
        this.gogokoaDa = gogokoaDa;
        this.sortzaileID = sortzaileID;
        this.url = pUrl;
        this.favs = pFavs;
        this.komentarioKop = pKomentario;
        this.views = pViews;
        this.etiketak = etiketaLista;
    }

    public Argazkia(String izena, String deskribapena, Integer id, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID, Integer favs, Integer komentarioKop) {
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.id = id;
        this.data = data;
        this.idFLickr = idFLickr;
        this.gogokoaDa = gogokoaDa;
        this.sortzaileID = sortzaileID;
        this.favs = favs;
        this.komentarioKop = komentarioKop;
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

    public Integer getViews() {
        return views;
    }

    public String emanStringEtiketak() {
        String emaitza = "";
        if (!etiketak.isEmpty()) {
            for (Etiketa e : etiketak) {
                emaitza += e.getIzena() + ", ";
            }
            return emaitza.substring(0, emaitza.length()-2);
        }
        return emaitza;
    }

    public void etiketenListaSartu(ArrayList<Etiketa> etiketenLista) {
        etiketak = etiketenLista;
    }

    public void editatuArgazkia(Date data, String izena, String deskribapena, String[] etiketak) {
        this.data = data;
        this.izena = izena;
        this.deskribapena = deskribapena;
        this.etiketak.clear();
        for (String e : etiketak) {
            Etiketa etik = new Etiketa(e);
            this.etiketak.add(etik);
        }
    }
}
