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
        String sql="CREATE TABLE IF NOT EXISTS "+NomTable+" (\n"
                +" id integer PRIMARY KEY,\n"
                +" name text NOT NULL,\n"
                +" capacity real\n"
                +");";

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

    public static void main(String[] args) {
        connect();
        addtable("UserConversation");

    }
}
