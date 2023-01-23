package network;

import java.util.List;

public interface UDPObserver {

    public void updateFromUDP(String action, List<String> data);
}
