package user;

import network.Message;
import network.ReceiverThread;
import network.TransmitterThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Conversation {

    private ReceiverThread receiverThread;

    private TransmitterThread transmitterThread;
    private User user;

    private ArrayList<Message> messageList;
    public Conversation(ReceiverThread receiverThread, TransmitterThread transmitterThread, User user)
    {
        this.receiverThread = receiverThread;
        this.transmitterThread = transmitterThread;
        this.user = user;
        this.messageList = new ArrayList<Message>();
    }

    public void addMessage(Message message) {
        this.messageList.add(message);
    }

    public User getUser() {
        return this.user;
    }
}
