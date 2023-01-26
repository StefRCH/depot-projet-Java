package network;

import java.net.InetAddress;
import java.util.List;

public interface TCPObserver {

    public void updateFromTCP(String action, String ip, Message message);

    public void updateFromTCPManager(String action, Thread receiver, Thread transmit, InetAddress ip);


}
