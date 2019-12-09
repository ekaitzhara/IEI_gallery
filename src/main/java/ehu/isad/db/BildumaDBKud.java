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

    public ArrayList<Bilduma> emanListaBildumarentzakoDatuGuztiak() {
        ArrayList<Bilduma> emaitza = new ArrayList<>();

        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT b.idBilduma, b.izena AS bildumaIzen, b.deskribapena AS bildumaDesk, b.sortzaileId," +
                " a.idArgazkia, a.izena AS argazkiIzen, a.deskribapena AS argazkiDesk, a.size, a.data, a.favs, a.komentarioKop, a.idFlickr, a.gogokoaDa" +
                " FROM Bilduma b, Argazkia a, BildumaArgazki ba WHERE ba.idBilduma=b.idBilduma AND a.idArgazkia=ba.idArgazkia" +
                " GROUP BY ba.idBilduma";
        rs = dbKud.execSQL(query);

        String bildumaAux = null;
        Bilduma b = null;
        try {
            while (rs.next()) {
                String idBilduma = rs.getString("idBilduma");
                String bildumaIzen = rs.getString("bildumaIzen");
                String bildumaDesk = rs.getString("bildumaDesk");
                String sortzaileId = rs.getString("sortzaileId");

                Integer idArgazkia = rs.getInt("idArgazkia");
                String argazkiIzen = rs.getString("argazkiIzen");
                String argazkiDesk = rs.getString("argazkiDesk");
                String size = rs.getString("size");
                Date data = rs.getDate("data");
                Integer idFlickr = rs.getInt("idFlickr");
                Integer favs = rs.getInt("favs");
                Integer komentarioKop = rs.getInt("komentarioKop");
                String s_gogokoaDa = rs.getString("gogokoaDa");
                Boolean gogokoaDa = s_gogokoaDa.equals("bai");

                if (!idBilduma.equals(bildumaAux)) {
                    if (b != null)
                        emaitza.add(b);
                    b = new Bilduma(bildumaIzen, idBilduma, bildumaDesk, sortzaileId);
                    bildumaAux = idBilduma;
                }

                //b.argazkiaGehitu(new Argazkia(argazkiIzen, argazkiDesk, data, idFlickr, gogokoaDa, sortzaileId, null, favs, komentarioKop, ETIKETAK));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emaitza;
    }



}
