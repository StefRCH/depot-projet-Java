package network;

import java.net.*;
import java.util.Enumeration;

/**
 * Cette classe sert à récupérer notre adresse IPv4 (et pas les autres comme IPv6, MAC, loopback, ...)
 * Elle permet d'éviter la répétition de ce gros morceau de code dans différentes classes
 * La création de la classe est une manière de le factoriser
 */
public class IPv4 {
    private InetAddress notreIP;

    //Méthode qui nous retourne notre adresse IPv4
    public InetAddress getIPv4() {
        // Ce code permet de récuperer notre IPv4. Le simple getHostAddress ne fonctionne pas sur les PC de l'INSA
        try {
            Enumeration<NetworkInterface> networkInterfaceEnumeration = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaceEnumeration.hasMoreElements()) {
                for (InterfaceAddress interfaceAddress : networkInterfaceEnumeration.nextElement().getInterfaceAddresses())
                    if (interfaceAddress.getAddress().isSiteLocalAddress()) {
                        this.notreIP = InetAddress.getByName(interfaceAddress.getAddress().getHostAddress());
                    }
            }
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        return this.notreIP; //.toString().substring(1) si on la veut en String et sans le "/" au début
    }

}
