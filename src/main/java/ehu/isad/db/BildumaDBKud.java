package ehu.isad.db;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BildumaDBKud {

    // singleton patroia
    private static BildumaDBKud instantzia = new BildumaDBKud();
    private static String idErab;

    private BildumaDBKud() {

    }

    public static BildumaDBKud getInstantzia() {
        return instantzia;
    }

    public boolean bildumaDago(String id) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT * FROM Bilduma WHERE idBilduma='" + id + "'";
        rs = dbKud.execSQL(query);

        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void bildumaSartu(String idBilduma, String izen, String erabiltzaile, String desk) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO Bilduma(idBilduma, izena, sortzaileId, deskribapena) VALUES('"+ idBilduma +"', '"+ izen +"', '"+erabiltzaile+"', '"+desk+"')";
        dbKud.execSQL(query);
    }


}
