package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPNetworkManager {
    public void launchTCPServer() throws IOException {
        ServerSocket socket = new ServerSocket(4000);
        Socket link = socket.accept();
    }

}
