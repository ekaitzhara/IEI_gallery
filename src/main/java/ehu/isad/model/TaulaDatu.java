package ehu.isad.model;

import ehu.isad.flickrKud.Utils;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.Date;


public class TaulaDatu {

    private Integer argazkiId;
    private Image argazkia;
    private String izena;
    private String etiketak;
    private Date data;
    private String deskribapena;
    private Integer favs;
    private Integer comments;

    public TaulaDatu(Integer pArgazkiId, String argazkiapath, String izena, String etiketak, Date data, String desk, Integer favs, Integer comments) {
        InputStream targetStream = null;
        try {
            targetStream = new FileInputStream(new File(argazkiapath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.argazkiId = pArgazkiId;
        this.argazkia = new Image(targetStream);
        this.izena = izena;
        this.etiketak = etiketak;
        this.data = data;
        this.deskribapena = desk;
        this.favs = favs;
        this.comments = comments;
    }

    public Integer getArgazkiId() {
        return argazkiId;
    }

    public Image getArgazkia() {
        return argazkia;
    }

    public String getIzena() {
        return izena;
    }

    public String getEtiketak() {
        return etiketak;
    }

    public Date getData() {
        return data;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    public Integer getFavs() {
        return favs;
    }

    public Integer getComments() {
        return comments;
    }
}
