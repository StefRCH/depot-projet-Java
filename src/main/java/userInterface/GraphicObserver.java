package userInterface;

import java.io.IOException;

public interface GraphicObserver {
    public void updateFromGUI(String action, String pseudo) throws IOException;
}
