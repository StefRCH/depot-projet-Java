package user;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;

public class UserManagerTest extends TestCase {

    //Variables pour tests
    private UserManager userManager;

    private String user1 = "John";
    private String ip1 = "127.0.0.1";

    public void setUserManager() throws IOException {
        userManager = new UserManager();
    }


    // ----- Test pour la méthode Update() ----- //
    @Test
    public void testUpdate() throws IOException, CloneNotSupportedException {
        setUserManager();
        List<String> dataList = new ArrayList<>();
        dataList.add("c/John/192.168.1.1");
        userManager.update(dataList);
        //Tests de vérification pour la connexion et les données de cette connexion
        assertTrue(userManager.getUsers().size() == 1);
        assertTrue(userManager.getUsers().get(0).getPseudo().equals("John"));
        assertTrue(userManager.getUsers().get(0).getIpAddress().toString().equals("/192.168.1.1"));

        dataList.clear();
        dataList.add("d/John/192.168.1.1");
        userManager.update(dataList);
        dataList.clear();
        //Test de vérification pour la déconnexion
        assertTrue(userManager.getUsers().size() == 0);

        dataList.add("c/John/192.168.1.1");
        userManager.update(dataList);
        dataList.clear();

        dataList.add("c/Tom/192.168.1.2");
        userManager.update(dataList);
        dataList.clear();

        dataList.add("m/Jane/192.168.1.255");
        userManager.update(dataList);
        dataList.add("g/Jane/192.168.1.1");
        userManager.update(dataList);
        dataList.clear();
        //Tests de vérification pour une nouvelle connexion, un changement de pseudo et la cohérence des données sur ce changement
        assertTrue(userManager.getUsers().size() == 2);
        assertTrue(userManager.getUsers().get(0).getPseudo().equals("Jane"));
        assertTrue(userManager.getUsers().get(0).getIpAddress().toString().equals("/192.168.1.1"));
    }


    // ----- Tests pour la méthode checkUser() ----- //
    @Test
    public void checkUser_ShouldReturnTrue_WhenPseudoIsAvailable() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        assertTrue(userManager.checkUser(user1, ip));
    }

    @Test
    public void checkUser_ShouldReturnFalse_WhenPseudoIsNotAvailable() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        userManager.createUser(user1, ip);
        assertFalse(userManager.checkUser(user1, ip));
    }


    // ----- Tests pour la méthode createUser() ----- //
    @Test
    public void createUser_ShouldReturnTrue_WhenPseudoIsAvailable() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        assertTrue(userManager.createUser(user1, ip));
    }

    @Test
    public void createUser_ShouldReturnFalse_WhenPseudoIsNotAvailable() throws IOException {
        setUserManager();
        UserManager userManager = new UserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        userManager.createUser(user1, ip);
        assertFalse(userManager.createUser(user1, ip));
    }


    // ----- Test pour la méthode deleteUser() ----- //
    @Test
    public void deleteUser_ShouldReturnTrue_WhenPseudoHasBeenDeleted() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        userManager.createUser(user1, ip);
        assertTrue(userManager.deleteUser(user1, ip));
    }


    // ----- Tests pour la méthode sendTCP() ----- //
    @Test
    public void sendTCP_ShouldReturnTrue_WhenPseudoExists() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        userManager.createUser(user1, ip);
        assertTrue(userManager.sendTCP(user1));
    }

    @Test
    public void sendTCP_ShouldReturnFalse_WhenPseudoDoesNotExist() throws IOException {
        setUserManager();
        assertFalse(userManager.sendTCP(user1));
    }


    /**
     * Il n'est pas vraiment possible dans le cadre de notre code de réaliser des tests unitaires sur les méthodes send (pour UDP)
     * En effet, aucun paramètre (quand il y en a) va réellement influencer le corps de la méthode
     * Au final, tout se passe dans la méthode createDatagram() que nous avons créé afin d'éviter de répéter le code de création et d'envoi de paquets
       dans toutes les méthodes send (pour UDP)
     */

    // ----- Tests pour la méthode createDatagramUDP() ----- //
    @Test
    public void createDatagramUDP_ShouldReturnDatagramPacket_WithCorrectData() throws Exception {
        UserManager userManager = new UserManager();
        String message = "c/John/127.0.0.1";
        DatagramPacket packet = userManager.createDatagramUDP(user1 ,ip1, message);
        assertArrayEquals(message.getBytes(), packet.getData());
        assertEquals(ip1, packet.getAddress());
    }

    @Test
    public void createDatagramUDP_ShouldReturnDatagramPacket_WithCorrectLength() throws Exception {
        UserManager userManager = new UserManager();
        InetAddress ip = InetAddress.getByName("127.0.0.1");
        String message = "c/John/127.0.0.1";
        DatagramPacket packet = userManager.createDatagramUDP(user1, ip1, message);
        assertEquals(message.getBytes().length, packet.getLength());
    }
}