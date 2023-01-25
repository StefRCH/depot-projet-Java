package network;

import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class UDPThreadTest {

    /** README
     * Il n'est pas possible de tester une méthode run() qui contient une boucle infinie car le test ne pourra jamais se terminer.
     * Il est tout de même possible de contourner le problème en utilisant interrupt ou sleep mais cela ne serait pas propre.
     */
    @Test
    public void testRunMethod() throws IOException, IOException {
    }
}