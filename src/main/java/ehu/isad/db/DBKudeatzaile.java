package ehu.isad.db;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Properties;

public class DBKudeatzaile {

    Connection conn = null;

    private void conOpen() throws IOException {

        Properties properties = null;
        InputStream in = null;

        try {
            in = this.getClass().getResourceAsStream("/setup.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }

        try {
            String url = "jdbc:mysql://localhost:3306/";
            String dbname = "eurobisioa?";
            String time = "useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
            String pk = "&allowPublicKeyRetrieval=true";
            System.out.println("voy por conexion");
            Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
            System.out.println("1");
            conn = DriverManager.getConnection(url+dbname+time+pk, "dasi2019", "dasi2019");
            System.out.println("2");
            conn.setCatalog(properties.getProperty("dbname"));
            System.out.println("hecho");

        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private ResultSet query(Statement s, String query) {

        ResultSet rs = null;

        try {
            s.executeQuery(query);
            rs = s.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    // singleton patroia
    private static DBKudeatzaile instantzia = new DBKudeatzaile();

    private DBKudeatzaile() {
        try {
            this.conOpen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DBKudeatzaile getInstantzia() {
        return instantzia;
    }

    public ResultSet execSQL(String query) {
        int count = 0;
        Statement s = null;
        ResultSet rs = null;
        try {
            s = (Statement) conn.createStatement();
            if (query.toLowerCase().indexOf("select") == 0) {
                // select agindu bat
                rs = this.query(s, query);
            } else {
                // update, delete, create agindu bat
                count = s.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
}