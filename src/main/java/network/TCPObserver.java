package network;

import java.util.List;

public interface TCPObserver {

    public void updateFromTCP(String action, String ip, Message message);

}
