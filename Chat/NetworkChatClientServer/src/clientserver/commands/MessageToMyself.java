package clientserver.commands;

import java.io.Serializable;

public class MessageToMyself implements Serializable {

    private final String sender;
    private final String receiver;
    private final String message;

    public MessageToMyself(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        sender = receiver;
        this.message = message;
    }


    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
