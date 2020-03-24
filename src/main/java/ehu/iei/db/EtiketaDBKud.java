package ehu.iei.db;

import ehu.isad.model.Etiketa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EtiketaDBKud {
    // singleton patroia
    private static EtiketaDBKud instantzia = new EtiketaDBKud();
    private static String idErab;

    private EtiketaDBKud() {

    }

    public static EtiketaDBKud getInstantzia() {
        return instantzia;
    }

    public void etiketaSartu(Integer idEtiketa, String izena) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO Etiketa(idEtiketa, izena) " +
                "VALUES('"+ idEtiketa +"', '"+ izena +"')";
        dbKud.execSQL(query);
    }

    public void etiketaArgazkianSartu(Integer idEtiketa, Integer idArgazkia) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "INSERT INTO ArgazkiEtiketak(Argazkia_idArgazkia, Etiketa_idEtiketa) " +
                "VALUES('"+ idArgazkia +"', '"+ idEtiketa +"')";
        dbKud.execSQL(query);
    }

    public ArrayList<Etiketa> etiketakEman(Integer idArgazki) {
        ArrayList<Etiketa> emaitza = new ArrayList<>();

        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT e.idEtiketa, e.izena FROM ArgazkiEtiketak ae, Etiketa e" +
                " WHERE ae.Argazkia_idArgazkia='"+idArgazki+"' AND ae.Etiketa_idEtiketa=e.idEtiketa";
        rs = dbKud.execSQL(query);


        try {
            while (rs.next()) {

                Integer idEtiketa = rs.getInt("idEtiketa");
                String izena = rs.getString("izena");

                emaitza.add(new Etiketa(idEtiketa, izena));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emaitza;
    }

    public void etiketaGuztiakEzabatu() {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM Etiketa";
        dbKud.execSQL(query);
    }

    public void kenduArgazkiaEtiketatik(Integer argazkiId) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "DELETE FROM ArgazkiEtiketak WHERE Argazkia_idArgazkia='"+argazkiId+"'";
        dbKud.execSQL(query);
    }

    public String etiketaEmanIzenarekin(String eIzena) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        ResultSet rs=null;
        String query = "SELECT idEtiketa FROM Etiketa WHERE izena='"+eIzena+"'";
        rs = dbKud.execSQL(query);
        try {
            if (rs.next())
                return rs.getString("idEtiketa");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void editatuArgazkiarenEtiketak(Integer argazkiId, String etikId) {
        DBKudeatzaile dbKud = DBKudeatzaile.getInstantzia();
        String query = "UPDATE ArgazkiEtiketak SET Etiketa_idEtiketa='"+etikId+"' WHERE Argazkia_idArgazkia='"+argazkiId+"'";
        dbKud.execSQL(query);
    }
}
