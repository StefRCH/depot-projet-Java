package network;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    String payload;



    Date date;


    SimpleDateFormat s;




    public Message(String text, Date date)
    {
        this.payload = text;
        this.date = date;
        this.s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    }
    @Override
    public String toString() {
        return s.format(date) +  " : " +payload;
    }

    public String getPayload() {
        return payload;
    }

    public Date getDate() {
        return date;
    }
}
