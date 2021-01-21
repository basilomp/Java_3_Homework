package org.client;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class Controller {

    private Network network;
    private String recipient;
    private String sender;
    private Stage primaryStage;


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
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
