package BDD;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.text.SimpleDateFormat;

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

    public static void addtable(String NomTable,String Key1,String Key2,String Key3){
        String url ="jdbc:sqlite:messages.db";
        //SQL Statement for new table
        String sql="CREATE TABLE IF NOT EXISTS "+NomTable+"("+Key1+" TEXT,"+Key2+" text,"+Key3+" text,PRIMARY KEY("+Key1+","+Key2+"))";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
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

    public static void Allcol(String Table){
        String url ="jdbc:sqlite:messages.db";
        System.out.println("Table "+Table+" contient champs :");
        String col="PRAGMA table_info("+Table+");";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(col);
            while(rs.next()){
                System.out.println(rs.getString("name"));
            }
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void addColExt(String Nomtable,String Nomcol,String OtherTable,String OtherCol){
        String url ="jdbc:sqlite:messages.db";
        String sql ="ALTER TABLE "+Nomtable+" ADD COLUMN "+Nomcol+"TEXT REFERENCES "+OtherTable+"("+OtherCol+");";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void insertCom(){
        String url ="jdbc:sqlite:messages.db";
        String sql ="INSERT INTO messages(date,destiname,payload) VALUES(?,?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);
        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,ts);
            pstmt.setString(2,"Paul");
            pstmt.setString(3,"Au revoir.");
            pstmt.executeUpdate();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void selectall(){
        String url ="jdbc:sqlite:messages.db";
        String sql ="SELECT * FROM messages;";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println(rs.getString("date")+"\t|\t"+rs.getString("destiname")+"\t|\t"+rs.getString("payload"));
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    public static void delete(){
        String url ="jdbc:sqlite:messages.db";
        String sql ="DELETE FROM messages WHERE destiname='Paul';";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }


    public static void main(String[] args) {
        //addtable("message","date","payload");
        //addtable("messages","date","destiname","payload");
        //addColExt("message","ipdest","userconversation","ipdestination");
        //Alltables();
        //delete();
        //insertCom();
        //Allcol("messages");
        selectall();
        //Allcol("userconversation");
        //Deletetable("message");
        //Deletetable("userconversation");
    }
}
