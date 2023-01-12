package BDD;

import javax.xml.crypto.Data;
import java.sql.*;

public class Create {
    public static void createNewDatabase(String filename) {
        String url = "jdbc:sqlite:../" + filename;

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String [] args){
        createNewDatabase("SSSIT.db");
    }
}
