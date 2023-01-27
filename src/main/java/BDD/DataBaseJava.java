package BDD;

import network.Message;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DataBaseJava {
    /* //Fonction permettant de connecter l'application à la base de données
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
    }*/

    //méthode permettant de créer une table avec 3 clés pour une clé primaire
    public static void addtable(String NomTable,String Key1,String Key2,String Key3,String Key4){
        String url ="jdbc:sqlite:messages.db";
        //SQL Statement for new table
        String sql="CREATE TABLE IF NOT EXISTS "+NomTable+"("+Key1+" TEXT,"+Key2+" text,"+Key3+" text,"+Key4+" text, PRIMARY KEY("+Key1+","+Key2+","+Key3+"))";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    //méthode pour supprimer une table
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

    //méthode permettant de vérifier toutes les tables présentes dans la base de données
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

    //méthode permettant de recenser tout les attributs et les clés primaires d'une table précise
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

    //méthode appelée pour insérer dans la table messages les messages de ipsrc vers ipdest
    public static void insertCom(String ipdest,String ipsrc,String message){
        String url ="jdbc:sqlite:messages.db";
        String sql ="INSERT INTO messages(date,ipdest,ipsrc,payload) VALUES(?,?,?,?)";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String ts = sdf.format(timestamp);
        try{
            Connection conn = DriverManager.getConnection(url);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,ts);
            pstmt.setString(2,ipdest);
            pstmt.setString(3,ipsrc);
            pstmt.setString(4,message);
            pstmt.executeUpdate();

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //méthode permettant de relever tout les messages enregistrés sur la base de données par date
    public static void selectallbydate(){
        String url ="jdbc:sqlite:messages.db";
        String sql ="SELECT * FROM messages ORDER BY date;";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println(rs.getString("date")+"\t|\t"+rs.getString("ipdest")+"\t|\t"+rs.getString("ipsrc")+"\t|\t"+rs.getString("payload"));
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //méthode permettant de sélectionner tout les messages entre ipsrc et ipdest
    public static void selectconv(String notreIP, String ipdest){
        String url ="jdbc:sqlite:messages.db";
        String sql ="SELECT * FROM messages WHERE ((ipdest='"+notreIP+"') AND (ipsrc='"+ipdest+"'))OR((ipdest='"+ipdest+"') AND (ipsrc='"+notreIP+"')) ORDER BY date;";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while(rs.next()){
                System.out.println(rs.getString("date")+"\t|\t"+rs.getString("ipdest")+"\t|\t"+rs.getString("ipsrc")+"\t|\t"+rs.getString("payload"));
            }

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    //Méthodes permettant de retourner les informations nécessaires afin d'identifier message reçu par date entre les entités communicantes
    public static String selectMSGconv(String notreIP, String ipdest){
        String url ="jdbc:sqlite:messages.db";
        String sql ="SELECT payload, date, ipdest FROM messages WHERE ((ipdest='"+notreIP+"') AND (ipsrc='"+ipdest+"'))OR((ipdest='"+ipdest+"') AND (ipsrc='"+notreIP+"')) ORDER BY date;";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            String Result = new String();

            while(rs.next()){
                //System.out.println(rs.getString("payload")+" | "+rs.getString("date"));
                Result+=rs.getString("payload")+" | "+rs.getString("date")+" | "+rs.getString("ipdest")+"\n";
            }

            return Result;


        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;

    }


    //méthode écrite statiquement pour supprimer les messages effectués en local pour des tests
    public static void delete(){
        String url ="jdbc:sqlite:messages.db";
        String sql ="DELETE FROM messages WHERE ipdest='/127.0.0.1';";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);

        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

    }

    //exemple de traitement du résultat obtenu (partiel, il faut encore ajouter des étapes permettant de traiter le résultat de sorte à ce que l'interface graphique puisse afficher les messages lors d'une initalisation de conversation)
    public static void main(String[] args) {
        String Resultquery = selectMSGconv("/10.1.5.43", "/10.1.5.44");
        String[] firstdecomp = Resultquery.split("\n");
        for (String word : firstdecomp) {
            System.out.println(word);
        }
    }
}
