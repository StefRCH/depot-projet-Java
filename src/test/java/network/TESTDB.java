package network;

import BDD.DataBaseJava;
import org.junit.Test;

import java.sql.SQLException;

public class TESTDB {

    @Test
    public void Connect() throws SQLException{
        DataBaseJava DB = new DataBaseJava();
        DB.connect();

        assert true;
    }
}
