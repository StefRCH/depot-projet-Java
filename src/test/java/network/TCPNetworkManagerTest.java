package network;

import org.junit.Test;

import static org.junit.Assert.*;

public class TCPNetworkManagerTest {

    /** README
     * Il n'est pas possible de tester une méthode run() qui contient une boucle infinie car le test ne pourra jamais se terminer.
     * Il est tout de même possible de contourner le problème en utilisant interrupt ou sleep mais cela ne serait pas propre.
     */
    @Test
    public void testRunMethod() {
    }
}