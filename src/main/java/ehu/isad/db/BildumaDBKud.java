package ehu.isad.db;

import ehu.isad.model.Argazkia;
import ehu.isad.model.Bilduma;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public ArrayList<Bilduma> emanListaBildumak() {
        ArrayList<Bilduma> emaitza = new ArrayList<>();

        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT b.idBilduma, b.izena, b.deskribapena, b.sortzaileId FROM Bilduma b";
        rs = dbKud.execSQL(query);


        try {
            while (rs.next()) {
                String idBilduma = rs.getString("idBilduma");
                String izena = rs.getString("izena");
                String deskribapena = rs.getString("deskribapena");
                String sortzaileId = rs.getString("sortzaileId");

                emaitza.add(new Bilduma(izena, idBilduma, deskribapena, sortzaileId));
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emaitza;
    }

    public boolean bildumaHutsaDago() {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT idBilduma FROM BildumaArgazki WHERE idBilduma=0";
        rs = dbKud.execSQL(query);


        try {
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
