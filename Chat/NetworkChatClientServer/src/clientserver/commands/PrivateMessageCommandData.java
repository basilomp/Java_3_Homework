package clientserver.commands;

import java.io.Serializable;

public class PrivateMessageCommandData implements Serializable {

    private final String receiver;
    private final String message;
    private final String sender;


    public PrivateMessageCommandData(String sender, String receiver, String message) {
        this.receiver = receiver;
        this.message = message;
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }
}
