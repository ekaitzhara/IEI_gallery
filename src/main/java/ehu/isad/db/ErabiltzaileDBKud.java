package ehu.isad.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ErabiltzaileDBKud {

    // singleton patroia
    private static ErabiltzaileDBKud instantzia = new ErabiltzaileDBKud();
    private static String idErab;

    private ErabiltzaileDBKud() {

    }

    public static ErabiltzaileDBKud getInstantzia() {
        return instantzia;
    }

    public void sartuErabiltzailea(String id, String izen) {
        idErab = id;
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO Erabiltzailea(idErabiltzailea, izena) VALUES('"+ id +"', '"+ izen +"')";
        dbKud.execSQL(query);

    }

    public static String getIdErab() {
        return idErab;
    }
}