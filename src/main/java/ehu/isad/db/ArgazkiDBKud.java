package ehu.isad.db;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public void argazkiaSartu(Integer id, String izena, String deskribapena, String size, Date data, String idFLickr, boolean gogokoaDa, String sortzaileID) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO Argazkia(idArgazkia, deskribapena, size, izena, data, sortzaileId, idFlickr, gogokoaDa) " +
                "VALUES('"+ id +"', '"+ deskribapena +"', '"+size+"', '"+izena+"', '"+data+"','"+sortzaileID+"', '"+idFLickr+"', '"+gogokoaDa+"')";
        dbKud.execSQL(query);
    }

    public void argazkiaBildumanSartu(Integer idArgazki, String idBilduma) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO BildumaArgazki(idBilduma, idArgazkia) " +
                "VALUES('"+ idArgazki +"', '"+ idBilduma +"')";
        dbKud.execSQL(query);

    }

    public void idFlickrSartu(String idArgazki, String idFLickr) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "UPDATE Argazkia SET idFlickr=" +idFLickr+ " WHERE idArgazkia=" +idArgazki;
        dbKud.execSQL(query);
    }
}
