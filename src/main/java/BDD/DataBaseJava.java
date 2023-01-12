package BDD;

import javax.swing.plaf.nimbus.State;
import java.sql.*;

public class DataBaseJava {
    public static void connect(){
        Connection conn = null;
        try {
            //db parameters
            String url = "jdbc:sqlite:messages.db";
            //create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQL has been established");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn!=null) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void addtable(String NomTable){
        String url ="jdbc:sqlite:messages.db";

        //SQL Statement for new table
        String sql="CREATE TABLE IF NOT EXISTS "+NomTable;

        String tables="SELECT name FROM sqlite_schema WHERE type='table' ";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(tables);


            while(rs.next()){
                System.out.println(rs.getString("name"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    public static void Deletetable(String NomTable){
        String url ="jdbc:sqlite:messages.db";
        String sql="DROP TABLE IF EXISTS "+NomTable;

        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void Alltables(){
        String url ="jdbc:sqlite:messages.db";
        String tables="SELECT name FROM sqlite_schema WHERE type='table' ";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(tables);
            while(rs.next()){
                System.out.println(rs.getString("name"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void addCol(String Nomtable,String Nomcol,String Typecol){
        String url ="jdbc:sqlite:messages.db";
        String sql ="ALTER TABLE IF EXISTS "+Nomtable+" ADD "+Nomcol+" "+Typecol;
    }
    public static void main(String[] args) {
        connect();


    }
}
