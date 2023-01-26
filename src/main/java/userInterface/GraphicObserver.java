package userInterface;

import network.Message;

import java.io.IOException;

public interface GraphicObserver {
    public void updateFromGUI(String action, String pseudo, Message message) throws IOException;
}
