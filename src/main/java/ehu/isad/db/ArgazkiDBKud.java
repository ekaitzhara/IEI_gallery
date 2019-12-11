package ehu.isad.db;

import ehu.isad.model.Argazkia;
import ehu.isad.model.Bilduma;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public void argazkiaSartu(Integer id, String izena, String deskribapena, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO Argazkia(idArgazkia, deskribapena, izena, data, sortzaileId, idFlickr, gogokoaDa) " +
                "VALUES('"+ id +"', '"+ deskribapena +"', '"+izena+"', '"+data+"','"+sortzaileID+"', '"+idFLickr+"', '"+gogokoaDa+"')";
        dbKud.execSQL(query);
    }

    public void argazkiaBildumanSartu(Integer idArgazki, String idBilduma) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO BildumaArgazki(idBilduma, idArgazkia) " +
                "VALUES('"+ idBilduma +"', '"+ idArgazki +"')";
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
                Integer idFlickr = rs.getInt("idFlickr");
                emaitza.add(idFlickr.toString());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return emaitza;
    }

    public void argazkiaEzabatu(String ezabatzekoID) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM Argazkia WHERE idFlickr=" +ezabatzekoID;
        dbKud.execSQL(query);
    }

    public ArrayList<Argazkia> emanArgazkiakBildumarekin(String idBilduma) {
        ArrayList<Argazkia> emaitza = new ArrayList<>();

        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT a.idArgazkia, a.izena, a.deskribapena, a.size, a.data, a.favs, a.komentarioKop, a.idFlickr as idFlickr, a.sortzaileId, a.gogokoaDa" +
                " FROM BildumaArgazki ba, Argazkia a" +
                " WHERE ba.idArgazkia=a.idArgazkia AND ba.idBilduma='"+idBilduma+"'";
        rs = dbKud.execSQL(query);


        try {
            while (rs.next()) {

                Integer idArgazkia = rs.getInt("idArgazkia");
                String izena = rs.getString("izena");
                String deskribapena = rs.getString("deskribapena");
                String sortzaileId = rs.getString("sortzaileId");
                String size = rs.getString("size");
                //Date data = rs.getDate("data");
                Integer idFlickr = rs.getInt("idFlickr");
                Integer favs = rs.getInt("favs");
                Integer komentarioKop = rs.getInt("komentarioKop");
                String s_gogokoaDa = rs.getString("gogokoaDa");
                Boolean gogokoaDa = s_gogokoaDa.equals("bai");

                //emaitza.add(new Argazkia(izena, deskribapena, idArgazkia, data, idFlickr.toString(), gogokoaDa, sortzaileId, favs, komentarioKop));
                String idf = idFlickr.toString();
                System.out.println("IdFlickr  string => " + idf);
                System.out.println("IdFlickr integer => " + idFlickr);
                emaitza.add(new Argazkia(izena, deskribapena, idArgazkia, null, idFlickr.toString(), gogokoaDa, sortzaileId, favs, komentarioKop));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emaitza;

    }
}
