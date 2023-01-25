package user;

import network.Message;

public interface ConversationObserver {
    public void updateFromConv(String action, String pseudo, Message message);

}
