package ehu.isad.db;

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

}
