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

    public void bildumaSartu(String izen, String idFlickr,  String erabiltzaile, String desk) {
        //Bilduma berria sortzen da
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO Bilduma(izena, idBilduma, sortzaileId, deskribapena) VALUES('"+ izen +"', '"+ idFlickr +"', '"+erabiltzaile+"', '"+desk+"')";
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


    public ArrayList<String> argazkiarenBildumak(String id){
        // id bat emanez argazkiaren bilduma lortzen du
        ArrayList<String> emaitza = new ArrayList<>();
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query =      "SELECT bil.izena " +
                            "FROM Bilduma bil, BildumaArgazki " +
                            "Where BildumaArgazki.idArgazkia="+id+" and bil.idBilduma=BildumaArgazki.idBilduma";

        rs = dbKud.execSQL(query);
        try {
            while (rs.next()) {
                String izena = rs.getString("izena");
                emaitza.add(izena);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emaitza;
    }

    public void argazkiaBildumanSartu(String bildumaIzena,String idArgazkia){
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO BildumaArgazki(bildumaIzena, idArgazkia) VALUES('"+ bildumaIzena +"', '"+ idArgazkia +")";
        dbKud.execSQL(query);
    }


    public void bildumaGuztiakEzabatu() {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM Bilduma";
        dbKud.execSQL(query);
    }

    public void bildumaArgazkiLoturakEzabatu() {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM BildumaArgazki";
        dbKud.execSQL(query);
    }

    public void kenduArgazkiaBildumatik(Integer argazkiId) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM BildumaArgazki WHERE idArgazkia='"+argazkiId+"'";
        dbKud.execSQL(query);
    }

    public String emanBildumaIzenarekin(String bilduma) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT idBilduma FROM Bilduma WHERE izena='" + bilduma + "'";
        rs = dbKud.execSQL(query);

        try {
            if (rs.next())
                return rs.getString("idBilduma");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void ezabatuBilduma(String bilduma) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM Bilduma WHERE izena='"+bilduma+"'";
        dbKud.execSQL(query);
    }

    public void argazkiaBildumazAldatu(String izenBilduma, String idArgazki) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "UPDATE BildumaArgazki SET idBilduma='"+izenBilduma+"' WHERE idArgazkia='"+idArgazki+"'";
        dbKud.execSQL(query);
    }
}
