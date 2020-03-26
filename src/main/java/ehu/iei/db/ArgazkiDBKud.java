package ehu.iei.db;

import ehu.iei.model.Argazkia;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ArgazkiDBKud {

    // singleton patroia
    private static ArgazkiDBKud instantzia = new ArgazkiDBKud();
    private static String idErab;

    private ArgazkiDBKud() {

    }

    public static ArgazkiDBKud getInstantzia() {
        return instantzia;
    }

    public boolean argazkiaDago(String id) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT * FROM Argazkia WHERE idArgazkia='" + id + "'";
        rs = dbKud.execSQL(query);

        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void argazkiaSartu(Integer idDB, String izena, String deskribapena, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID, Integer favs, Integer komentarioak) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO Argazkia(idArgazkia, deskribapena, izena, data, sortzaileId, idFlickr, gogokoaDa, favs, komentarioKop) " +
                "VALUES('"+ idDB +"', '"+ deskribapena +"', '"+izena+"', '"+data+"','"+sortzaileID+"', '"+idFLickr+"', '"+gogokoaDa+"', '"+favs+"', '"+komentarioak+"')";
        dbKud.execSQL(query);
    }
    public void argazkiaSartu(Argazkia photo){
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        Integer idDB = photo.getId();
        String izena = photo.getIzena();
        String deskribapena = photo.getDeskribapena();
        Date data = photo.getData();
        String idFLickr = photo.getIdFLickr();
        boolean gogokoaDa = photo.isGogokoaDa();
        String sortzaileID = photo.getSortzaileID();
        Integer favs = photo.getFavs();
        Integer komentarioak = photo.getKomentarioKop();
        String query = "INSERT INTO Argazkia(idArgazkia, deskribapena, izena, data, sortzaileId, idFlickr, gogokoaDa, favs, komentarioKop) " +
                "VALUES('"+ idDB +"', '"+ deskribapena +"', '"+izena+"', '"+data+"','"+sortzaileID+"', '"+idFLickr+"', '"+gogokoaDa+"', '"+favs+"', '"+komentarioak+"')";
        dbKud.execSQL(query);
    }

    public void argazkiaBildumanSartu(Integer idArgazki, String izenBilduma) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO BildumaArgazki(idBilduma, idArgazkia) " +
                "VALUES('"+ izenBilduma +"', '"+ idArgazki +"')";
        dbKud.execSQL(query);

    }

    public void idFlickrSartu(String idArgazki, String idFLickr) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "UPDATE Argazkia SET idFlickr=" +idFLickr+ " WHERE idArgazkia=" +idArgazki;
        dbKud.execSQL(query);
    }

    public ArrayList<String> emanIdFlickrGuztiak() {
        ArrayList<String> emaitza = new ArrayList<>();
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "SELECT idFlickr FROM Argazkia";
        ResultSet rs = dbKud.execSQL(query);

        try {
            while (rs.next()) {
                String idFlickr = rs.getString("idFlickr");
                emaitza.add(idFlickr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emaitza;
    }

    public void argazkiaEzabatuIdFlickrrekin(String ezabatzekoID) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM Argazkia WHERE idFlickr=" +ezabatzekoID;
        dbKud.execSQL(query);
    }

    public ArrayList<Argazkia> emanArgazkiakBildumarekin(String izenBilduma) {
        ArrayList<Argazkia> emaitza = new ArrayList<>();

        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT a.idArgazkia, a.izena, a.deskribapena, a.size, a.data, a.favs, a.komentarioKop, a.idFlickr as idFlickr, a.sortzaileId, a.gogokoaDa" +
                " FROM BildumaArgazki ba, Argazkia a" +
                " WHERE ba.idArgazkia=a.idArgazkia AND ba.idBilduma='"+izenBilduma+"'";
        rs = dbKud.execSQL(query);


        try {
            while (rs.next()) {

                Integer idArgazkia = rs.getInt("idArgazkia");
                String izena = rs.getString("izena");
                String deskribapena = rs.getString("deskribapena");
                String sortzaileId = rs.getString("sortzaileId");
                String size = rs.getString("size");
                String d = rs.getString("data");
                Date data = null;
                if (!d.equals("null"))
                    data = Date.valueOf(d);
                String idFlickr = rs.getString("idFlickr");
                Integer favs = rs.getInt("favs");
                Integer komentarioKop = rs.getInt("komentarioKop");
                String s_gogokoaDa = rs.getString("gogokoaDa");
                Boolean gogokoaDa = s_gogokoaDa.equals("bai");

                //emaitza.add(new Argazkia(izena, deskribapena, idArgazkia, data, idFlickr.toString(), gogokoaDa, sortzaileId, favs, komentarioKop));
                emaitza.add(new Argazkia(izena, deskribapena, idArgazkia, data, idFlickr, gogokoaDa, sortzaileId, favs, komentarioKop));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emaitza;

    }

    public String emanIdFlickr(Integer idArgazkiDB) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT idFlickr FROM Argazkia" +
                " WHERE idArgazkia='"+idArgazkiDB+"'";
        rs = dbKud.execSQL(query);
        try {
            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void argazkiGuztiakEzabatu() {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM Argazkia";
        dbKud.execSQL(query);
    }

    public void argazkiEtiketaLoturakEzabatu() {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM ArgazkiEtiketak";
        dbKud.execSQL(query);
    }

    public HashMap emanPhotosToUpload() {
        HashMap emaitza = new HashMap<String,String>();
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "SELECT * FROM PhotosToUpload";
        ResultSet rs = dbKud.execSQL(query);
        if(rs!=null){
            try {
                while (rs.next()) {
                    Integer idArgazkiaDB = rs.getInt("idArgazkia");
                    String bildumaIzena = rs.getString("bildumaIzena");
                    String argazkiIzena = rs.getString("argazkiIzen");
                    emaitza.put(argazkiIzena,idArgazkiaDB);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return emaitza;
    }

    public ArrayList<String> emanPhotosToDelete() {
        ArrayList<String> emaitza = new ArrayList<>();
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "SELECT * FROM PhotosToDelete";
        ResultSet rs = dbKud.execSQL(query);
        if(rs!=null){
            try {
                while (rs.next()) {
                    String idFlickr = rs.getString("idFlickr");
                    emaitza.add(idFlickr);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return emaitza;
    }

    public void clearPhotosToDelete() {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM PhotosToDelete";
        dbKud.execSQL(query);
    }
    public void clearPhotosToUpload() {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM PhotosToUpload";
        dbKud.execSQL(query);
    }


    public void addPhotoToUpload(String argazkiIzena, String idArgazki, String izenBilduma) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO PhotosToUpload(idArgazkia, bildumaIzena, argazkiIzen) " +
                "VALUES('"+ idArgazki +"', '"+ izenBilduma +"', '"+argazkiIzena+"')";
        dbKud.execSQL(query);
    }

    public void addPhotoToDelete(String id) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO PhotosToDelete(idFlickr) " +
                "VALUES('"+ id +"')";
        dbKud.execSQL(query);
    }

    public void kenduBildumaArgazkiatik(String bilduma) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "UPDATE BildumaArgazki SET idBilduma='NotInASet' WHERE idBilduma='"+bilduma+"'";
        dbKud.execSQL(query);
    }

    public void editatuDatuak(Integer argazkiId, Date data, String izena, String deskribapena) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "UPDATE Argazkia SET izena='"+izena+"', data='"+data+"', deskribapena='"+deskribapena+"'" +
                ", komentarioKop=komentarioKop+1 WHERE idArgazkia='"+argazkiId+"'";
        dbKud.execSQL(query);
    }

    public String emanIdDB(String idFlickr) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT idArgazkia FROM Argazkia" +
                " WHERE idFlickr='"+idFlickr+"'";
        rs = dbKud.execSQL(query);
        try {
            if (rs.next()) {
                return rs.getString("idArgazkia");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
