package network;

import BDD.DataBaseJava;
import org.junit.Before;
import org.junit.Test;

import java.sql.*;

public class TESTDB {

    @Before
    public void clearDB(){
        String url ="jdbc:sqlite:messages.db";
        String sql="DROP TABLE *";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testCREATETABLE(){
        String url ="jdbc:sqlite:messages.db";
        //SQL Statement for new table
        String sql="CREATE TABLE IF NOT EXISTS TEST(id INTEGER PRIMARY KEY,bla text)";
        String tables="SELECT name FROM sqlite_schema WHERE type='table' ";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(tables);
            boolean testf=false;
            while(rs.next()){
                String Test=rs.getString("name");
                if(Test.equals("TEST")){
                    testf=true;
                }
            }
            assert testf;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testINSERT(){
        String url ="jdbc:sqlite:messages.db";
        //SQL Statement for new table
        String sql="INSERT INTO TEST(id,bla) VALUES(999,'DWBH')";
        String result="SELECT * FROM TEST WHERE id=999";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(result);
            boolean testf=false;
            while(rs.next()){
                String resid=rs.getString("id");
                String restxt=rs.getString("bla");
                if(resid.equals("999")&restxt.equals("DWBH")){
                    testf=true;
                }
            }
            assert testf;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }


    @Test
    public void testDELETE(){
        String url ="jdbc:sqlite:messages.db";
        //SQL Statement for new table
        String sql="DELETE FROM TEST WHERE id=999";
        String result="SELECT * FROM TEST WHERE id=999";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(result);
            boolean testf=true;
            while(rs.next()){
                String resid=rs.getString("id");
                String restxt=rs.getString("bla");
                if(resid.equals("999")&restxt.equals("DWBH")){
                    testf=false;
                }
            }
            assert testf;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void testDELETETABLE(){
        String url ="jdbc:sqlite:messages.db";
        //SQL Statement for new table
        String sql="DROP TABLE TEST";
        String tables="SELECT name FROM sqlite_schema WHERE type='table' ";
        try{
            Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            stmt.execute(sql);
            ResultSet rs = stmt.executeQuery(tables);
            boolean testf=true;
            while(rs.next()){
                String Test=rs.getString("name");
                if(Test.equals("TEST")){
                    testf=false;
                }
            }
            assert testf;
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }


}
