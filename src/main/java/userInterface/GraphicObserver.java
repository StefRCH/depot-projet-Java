package userInterface;

import java.io.IOException;

public interface GraphicObserver {
    public void update(String action, String pseudo) throws IOException;
}
