package user;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testGetPseudo() throws UnknownHostException {
        String testPseudo = "John Doe";
        InetAddress testIp = InetAddress.getByName("localhost");
        User user = new User(testPseudo, testIp);

        String result = user.getPseudo();

        assertEquals(testPseudo, result);
    }

    @Test
    public void testGetIpAddress() throws UnknownHostException {
        String testPseudo = "John Doe";
        InetAddress testIp = InetAddress.getByName("localhost");
        User user = new User(testPseudo, testIp);

        InetAddress result = user.getIpAddress();

        assertEquals(testIp, result);
    }

    @Test
    public void testSetPseudo() throws UnknownHostException {
        String testPseudo = "John Doe";
        String newPseudo = "Jane Doe";
        InetAddress testIp = InetAddress.getByName("localhost");
        User user = new User(testPseudo, testIp);

        user.setPseudo(newPseudo);
        String result = user.getPseudo();

        assertEquals(newPseudo, result);
    }
}