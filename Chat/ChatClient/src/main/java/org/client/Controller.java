package org.client;

import chat.MyServer;
import chat.User;
import chat.handler.ClientHandler;
import clientserver.Command;
import clientserver.CommandType;
import clientserver.commands.AuthOkCommandData;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Controller {

    private Network network;
    private String recipient;
    private String sender;
    private Stage primaryStage;
    private ClientHandler clientHandler;




    @FXML
    public void initialize() {
    participants.setItems(FXCollections.observableArrayList(ChatClient.USER_LIST));

    participants.setCellFactory(listView -> {
        MultipleSelectionModel<String> selectionModel = participants.getSelectionModel();
        ListCell<String> cell = new ListCell<>();
        cell.textProperty().bind(cell.itemProperty());
        cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            participants.requestFocus();
            if (!cell.isEmpty()) {
                int index = cell.getIndex();
                if (selectionModel.getSelectedIndices().contains(index)) {
                    selectionModel.clearSelection();
                    recipient = null;
                } else {
                    selectionModel.select(index);
                    recipient = cell.getItem();
                }
                event.consume();
            }
        });
        return cell;
    });

    }

    public TextField getTextField() {
        return textField;
    }

    @FXML
    private TextField textField;

    @FXML
    private TextArea chatLog;

    public TextArea getChatLog() {
        return chatLog;
    }

    public void setChatLog(TextArea chatLog) {
        this.chatLog = chatLog;
    }

    @FXML
    private Button textButton;

    @FXML
    public ListView<String> participants;


    @FXML
    public void sendMessage() {
        String message = textField.getText();
        appendMessage("Me: " + message);
        textField.clear();

        try {
            if (recipient != null) {
                network.sendPrivateMessage(sender, recipient, message);
            } else {
                network.sendMessage(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
            String errorMessage = "Failed to send message";
            ChatClient.showNetworkError(e.getMessage(), errorMessage, primaryStage);
        }
    }

    public void appendMessage(String message) {
        chatLog.appendText(message);
        chatLog.appendText(System.lineSeparator());
        Log.start(network.getNickname());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime time = LocalTime.now();
        String t = dtf.format(time);
        Log.writeToFile("[" + t + "] " + message);



    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    }
