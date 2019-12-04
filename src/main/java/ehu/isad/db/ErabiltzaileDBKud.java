package ehu.isad.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ErabiltzaileDBKud {

    // singleton patroia
    private static ErabiltzaileDBKud instantzia = new ErabiltzaileDBKud();
    private static String idErab;
    private static String izenErab;

    private ErabiltzaileDBKud() {

    }

    public static ErabiltzaileDBKud getInstantzia() {
        return instantzia;
    }

    public void sartuErabiltzailea(String id, String izen) {
        idErab = id;
        izenErab = izen;
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO Erabiltzailea(idErabiltzailea, izena) VALUES('"+ id +"', '"+ izen +"')";
        dbKud.execSQL(query);

        System.out.println(id + " , "  + izen + " sartuta datu basean");
    }

    public static String getIdErab() {
        return idErab;
    }

    public String emanIzena() {

        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "SELECT izena FROM Erabiltzailea";
        ResultSet rs = dbKud.execSQL(query);

        try {
            while (rs.next()) {
                String izen = rs.getString("izena");
                return izen;
            }
        } catch (SQLException e) {
            System.err.println(e);
        }
        return null;
    }
}
