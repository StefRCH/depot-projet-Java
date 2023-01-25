package user;

import network.TCPNetworkManager;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class UserManagerTest {

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
        Assert.assertTrue(userManager.getUsers().size() == 1);
        Assert.assertTrue(userManager.getUsers().get(0).getPseudo().equals("John"));
        Assert.assertTrue(userManager.getUsers().get(0).getIpAddress().toString().equals("/192.168.1.1"));

        dataList.clear();
        dataList.add("d/John/192.168.1.1");
        userManager.update(dataList);
        dataList.clear();
        //Test de vérification pour la déconnexion
        Assert.assertTrue(userManager.getUsers().size() == 0);

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
        Assert.assertTrue(userManager.getUsers().size() == 2);
        Assert.assertTrue(userManager.getUsers().get(0).getPseudo().equals("Jane"));
        Assert.assertTrue(userManager.getUsers().get(0).getIpAddress().toString().equals("/192.168.1.1"));
    }


    // ----- Tests pour la méthode checkUser() ----- //
    @Test
    public void checkUser_ShouldReturnTrue_WhenPseudoIsAvailable() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        Assert.assertTrue(userManager.checkUser(user1, ip));
    }

    @Test
    public void checkUser_ShouldReturnFalse_WhenPseudoIsNotAvailable() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        userManager.createUser(user1, ip);
        Assert.assertFalse(userManager.checkUser(user1, ip));
    }


    // ----- Test pour la méthode createUser() ----- //
    /**
     * On aurait pu tester le fait qu'un utilisateur ne puisse pas être créé à cause d'un utilisateur ayant déjà le pseudo
     * Cependant ceci est le rôle de la méthode checkUser() qui est toujours appelée avant un createUser()
     * Il aurait été possible d'appeler checkUser() dans createUser() mais cela aurait créé plusieurs possibilités d'erreur pour un return false
     * Il était donc plus simple et plus propre de bien séparer ces deux utilités càd --> vérifier l'unicité puis ajouter l'utilisateur en conséquence
     */
    @Test
    public void createUser_ShouldReturnTrue_WhenUserHasBeenAdded() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        Assert.assertTrue(userManager.createUser(user1, ip));
    }


    // ----- Test pour la méthode deleteUser() ----- //
    @Test
    public void deleteUser_ShouldReturnTrue_WhenUserHasBeenDeleted() throws IOException {
        setUserManager();
        InetAddress ip = InetAddress.getByName(ip1);
        userManager.createUser(user1, ip);
        Assert.assertTrue(userManager.deleteUser(user1, ip));
    }


    // ----- Tests pour la méthode sendTCP() ----- //
    @Test
    public void sendTCP_ShouldReturnTrue_WhenPseudoExists() throws IOException {
        setUserManager();
        TCPNetworkManager tcpNetworkManager = new TCPNetworkManager();
        tcpNetworkManager.start();
        InetAddress ip = InetAddress.getByName(ip1);
        userManager.createUser(user1, ip);
        Assert.assertTrue(userManager.sendTCP(user1));
    }

    @Test
    public void sendTCP_ShouldReturnFalse_WhenPseudoDoesNotExist() throws IOException {
        setUserManager();
        Assert.assertFalse(userManager.sendTCP(user1));
    }


    /**
     * Il n'est pas vraiment possible dans le cadre de notre code de réaliser des tests unitaires sur les méthodes send (pour UDP)
     * En effet, aucun paramètre (quand il y en a) va réellement influencer le corps de la méthode
     * Au final, tout se passe dans la méthode createDatagram() que nous avons créé afin d'éviter de répéter le code de création et d'envoi de paquets
       dans toutes les méthodes send (pour UDP)
     */

    // ----- Test pour la méthode createDatagramUDP() ----- //
    @Test
    public void createDatagramUDP_ShouldReturnDatagramPacket_WithCorrectData() throws Exception {
        setUserManager();
        String message = "c";
        DatagramPacket packet = userManager.createDatagramUDP(user1 ,ip1, message);
        Assert.assertEquals(ip1, packet.getAddress().toString().substring(1));
    }

    @Test
    public void testGetUsers() throws IOException {
        setUserManager();
        ArrayList<User> testUsers = new ArrayList<>();
        testUsers.add(new User("John Doe", InetAddress.getByName("localhost")));
        testUsers.add(new User("Jane Doe", InetAddress.getByName("127.0.0.1")));
        userManager.setUsers(testUsers);

        ArrayList<User> result = userManager.getUsers();

        Assert.assertEquals(testUsers, result);
    }
}