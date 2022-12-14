package network;

import java.io.IOException;
import java.net.Socket;

public class TransmitterThread {
    public void startConversation() throws IOException {
        Socket link = new Socket("127.0.0.1",4000);
        // .............. //
        link.close();
    }
}
