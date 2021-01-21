package chat;

import chat.auth.AuthService;
import chat.auth.BaseAuthService;
import chat.auth.DBAuthService;
import chat.handler.ClientHandler;
import chat.handler.DBHandler;
import clientserver.Command;
import org.client.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static clientserver.Command.updateUsersListCommand;

public class MyServer {

    private final List<ClientHandler> clients = new ArrayList<>();
    private AuthService authService;

    public MyServer() {
        this.authService = new BaseAuthService();
    }


    public void start(int port) throws IOException {
        if (!DBHandler.connectDB()) {
            throw new RemoteException("DB connection error");
        }
        authService = new DBAuthService();


        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started");
            runServerMessageThread();
            authService.start();
            //noinspection InfiniteLoopStatement
            while (true) {
                waitAndProcessNewClientConnection(serverSocket);
            }
        } catch (IOException e) {
            System.err.println("Failed to accept new connection");
            e.printStackTrace();
        } finally {
            DBHandler.disconnect();
            authService.stop();

        }
    }

    private void runServerMessageThread() {
        Thread serverMessageThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String serverMessage = scanner.next();
                try {
                    broadcastMessage("Server: " + serverMessage, null);
                } catch (Exception e) {
                    System.err.println("Failed to process server message");
                    e.printStackTrace();
                }
            }
        });
        serverMessageThread.setDaemon(true);
        serverMessageThread.start();
    }

    private void waitAndProcessNewClientConnection(ServerSocket serverSocket) throws IOException {
        System.out.println("Awaiting new connection");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Client connected");
        processClientConnection(clientSocket);
    }

    private void processClientConnection(Socket clientSocket) throws IOException {
        ClientHandler clientHandler = new ClientHandler(this, clientSocket);
        clientHandler.handle();
    }


    public synchronized void broadcastMessage(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : clients) {
            if (client == sender) {
                continue;
            }

            if (sender == null) {
                client.sendMessage(message);

            } else {
                client.sendMessage(sender.getNickname(), message);


            }

        }
    }

    public synchronized void subscribe(ClientHandler handler) throws IOException {
        clients.add(handler);
        notifyClientsUsersListUpdate(clients);
    }

    public synchronized void unsubscribe(ClientHandler handler) throws IOException {
        clients.remove(handler);
        notifyClientsUsersListUpdate(clients);
    }

    private void notifyClientsUsersListUpdate(List<ClientHandler> clients) throws IOException {
        List<String> usernames = new ArrayList<>();

        for (ClientHandler client : clients) {
            usernames.add(client.getNickname());

        }

        for (ClientHandler client : clients) {
            client.sendCommand(updateUsersListCommand(usernames));
        }
    }

    public AuthService getAuthService() {
        return authService;
    }

    public synchronized boolean isNickBusy(String nickname) {
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(nickname)) {
                return true;
            }
        }
        return false;
    }


    public synchronized void sendPrivateMessage(ClientHandler sender, String recipient, String message) throws IOException {
            for (ClientHandler client : clients) {
             if (client.getNickname().equals(recipient)) {
                    client.sendMessage(sender.getNickname(), ("To " + recipient + ": ") + message);

            }

            }
    }
    }



//    public synchronized void sendPrivateMessage(ClientHandler sender, String recipient, String message) throws IOException {
//        for (ClientHandler client : clients) {
//            if (client.getNickname().equals(recipient)) {
//                client.sendMessage(sender.getNickname(), "To [" + recipient + "]: " + message);
////                ("To [" + recipient + "] from [" + sender.getNickname() + "] " + message)
////            } else if (message.startsWith("/w")) {
////                String[] str = message.split("\\s+ ", 3);
////                str[1] = recipient;
////                str[2] = message;
////                client.sendMessage("To [" + recipient + "] from [" + sender.getNickname() + "] " + message);
////
//////
////
////            }
//
//            }
//        }
//    }

