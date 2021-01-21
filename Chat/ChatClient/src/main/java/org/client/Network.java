package org.client;

import clientserver.Command;
import clientserver.commands.*;
import javafx.application.Platform;

import java.io.*;
import java.net.Socket;

import static clientserver.Command.*;

public class Network {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 8180;



    private String host;
    private int port;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket socket;
    private ChatClient chatClient;
    private String nickname;


    public Network() {
        this(SERVER_ADDRESS, SERVER_PORT);
    }

    public Network(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Network(ChatClient chatClient) {
        this();
        this.chatClient = chatClient;
    }

    public boolean connect() {
        try {
            socket = new Socket(host, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Failed to establish connection");
            e.printStackTrace();
            return false;
        }
    }


    public void sendMessage(String message) throws IOException {
        sendCommand(Command.publicMessage(nickname, message));
    }

    public void sendPrivateMessage(String sender, String receiver, String message) throws IOException {
        sendCommand(Command.privateMessageCommand(sender, receiver, message));
    }

    private void sendCommand(Command command) throws IOException {
        outputStream.writeObject(command);
    }


    public void waitMessage(Controller controller) {
        Thread thread = new Thread(() -> {
            try {
                while (true) {
                    Command command = readCommand();
                    if (command == null) {
                        continue;
                    }
                    if (chatClient.getState() == ChatClientState.AUTHENTICATION) {
                        processAuthResult(command);

                    } else {
                        processMessage(controller, command);
                    }
                }
            } catch (IOException e) {
                System.err.println("Connection lost");
                e.printStackTrace();
            }

        });
        thread.setDaemon(true);
        thread.start();
    }

    private void processMessage(Controller controller, Command command) {
        switch (command.getType()) {
            case INFO_MESSAGE: {
                MessageInfoCommandData data = (MessageInfoCommandData) command.getData();
                Platform.runLater(() -> {
                    controller.appendMessage("Server: " + data.getMessage());
                });
                break;
            }
            case CLIENT_MESSAGE: {
                ClientMessageCommandData data = (ClientMessageCommandData) command.getData();
                String sender = data.getSender();
                String message = data.getMessage();
                Platform.runLater(() ->
                {
                    controller.appendMessage(String.format("%s: %s", sender, message));
                });
                break;
            }
            case ERROR: {
                ErrorCommandData data = (ErrorCommandData) command.getData();
                Platform.runLater(() -> {
                    ChatClient.showNetworkError(data.getErrorMessage(), "Server error", null);
                });
                break;
            }
            case UPDATE_USERS: {
                UpdateUsersListCommandData data = (UpdateUsersListCommandData) command.getData();
                Platform.runLater(() -> {
                    chatClient.updateUsers(data.getUsers());
                });
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown command type: " + command.getType());

        }
    }

    private void processAuthResult(Command command) {
        switch (command.getType()) {
            case AUTH_OK: {
                AuthOkCommandData data = (AuthOkCommandData) command.getData();
                nickname = data.getUsername();
                Platform.runLater(() -> {
                    chatClient.activeChatDialog(nickname);
                });
                break;

            }
            case ERROR: {
                ErrorCommandData data = (ErrorCommandData) command.getData();
                Platform.runLater(() -> {
                    ChatClient.showNetworkError(data.getErrorMessage(), "Auth error", null);
                });
                break;
            }
            default:
                throw new IllegalArgumentException("Unknown command type: " + command.getType());
        }
    }

    public void close() {
        try {
            if (socket != null && socket.isConnected()) {
            socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Command readCommand() throws IOException {
        Command command = null;
        try {
            command = (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to read Command class");
            e.printStackTrace();
        }
        return command;
    }


    public void sendAuthMessage(String login, String password) throws IOException {
        sendCommand(authCommand(login, password));
    }
}