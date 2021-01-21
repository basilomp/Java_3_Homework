package chat.handler;

import chat.MyServer;
import clientserver.Command;
import clientserver.CommandType;
import clientserver.commands.AuthCommandData;
import clientserver.commands.MessageToMyself;
import clientserver.commands.PrivateMessageCommandData;
import clientserver.commands.PublicMessageCommandData;

import java.io.*;
import java.net.Socket;

import static clientserver.Command.*;

public class ClientHandler {

    private final MyServer myServer;
    private final Socket clientSocket;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nickname;


    public ClientHandler(MyServer myServer, Socket clientSocket) {

        this.myServer = myServer;
        this.clientSocket = clientSocket;

    }

    public void handle() throws IOException {
        in = new ObjectInputStream(clientSocket.getInputStream());
        out = new ObjectOutputStream(clientSocket.getOutputStream());

        new Thread(() -> {
            try {
                authentication();
                readMessages();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    closeConnection();
                } catch (IOException e) {
                    System.err.println("Failed to close connection");
                }
            }
        }).start();
    }

    private void authentication() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }

            if (command.getType() == CommandType.AUTH) {
                AuthCommandData data = (AuthCommandData) command.getData();
                String login = data.getLogin();
                String password = data.getPassword();
                String nickname = myServer.getAuthService().getNickByLoginPass(login, password);
                if (nickname == null) {
                    sendCommand(errorCommand("Invalid credentials"));
                    continue;
                }

                if (myServer.isNickBusy(nickname)) {
                    sendCommand(errorCommand("Nickname is already used"));
                    continue;
                }

                sendCommand(authOkCommand(nickname));
                setNickname(nickname);
                myServer.broadcastMessage(String.format("'%s' joined chat", nickname), null);
                myServer.subscribe(this);
                return;
            }
        }

    }

    public void sendCommand(Command command) throws IOException {
        out.writeObject(command);
    }

    private Command readCommand() throws IOException {
        Command command = null;
        try {
            command = (Command) in.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to read Command class");
            e.printStackTrace();
        }
        return command;
    }

    private void closeConnection() throws IOException {
        myServer.unsubscribe(this);
        String unsubscribedClient = this.getNickname();
        clientSocket.close();
     }

     private void readMessages() throws IOException {
        while (true) {
            Command command = readCommand();

            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommandData data = (PrivateMessageCommandData) command.getData();
                    String receiver = data.getReceiver();
                    String message = data.getMessage();
                    if (receiver.equals(this.nickname)) {
                        break;
                    } else {
                    myServer.sendPrivateMessage(this, receiver, message);
                    }
                    break;
                }
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandData data = (PublicMessageCommandData) command.getData();
                    String message = data.getMessage();
                    myServer.broadcastMessage(message, this);
                    break;
                }
                case END:
                    return;
                default:
                    throw new IllegalArgumentException("Unknown command type: " + command.getType());
            }
        }
     }



     public void sendMessage(String message) throws IOException {
        sendCommand(messageInfoCommand(message));
     }

     public void sendMessage(String sender, String message) throws IOException {
        sendCommand(clientMessageCommand(sender, message));
     }



     public void sendPrivateMessage(String sender, String recipient, String message) throws IOException {
        sendCommand(privateMessageCommand(sender, recipient, message));
    }


    public String getNickname() {
        return nickname;
    }

    private void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
