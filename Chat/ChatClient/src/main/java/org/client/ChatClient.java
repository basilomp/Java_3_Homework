package org.client;

import clientserver.Command;
import clientserver.commands.AuthOkCommandData;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.client.AuthController;
import org.client.Controller;
import org.client.ChatClientState;
import org.client.Network;


import java.io.IOException;
import java.util.List;

/**
 * JavaFX App
 */
public class ChatClient extends Application {

    public static final List<String> USER_LIST = List.of("James", "Ulysses", "Finnegan");



    private static Scene scene;
    private ChatClientState state = ChatClientState.AUTHENTICATION;
    private Stage primaryStage;
    private Stage authDialogStage;
    private Network network;
    private Controller controller;
    private String clientNickname;

    public void updateUsers(List<String> users) {
        controller.participants.setItems(FXCollections.observableList(users));
    }


    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;


        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ChatClient.class.getResource("Sample.fxml"));
        Parent root = loader.load();
        controller = loader.getController();
        primaryStage.setTitle("Messenger");
        primaryStage.setScene(new Scene(root, 640, 480));
        controller.getTextField().requestFocus();




//        scene = new Scene(loadFXML("Sample"), 640, 504);
//        primaryStage.setScene(scene);
//        primaryStage.show();

        network = new Network(this);
        if (!network.connect()) {
            showNetworkError("", "Server connection failure", primaryStage);
        }

        controller.setNetwork(network);
        controller.setStage(primaryStage);


        network.waitMessage(controller);

        primaryStage.setOnCloseRequest(event -> {
            try {
                network.sendMessage("/end");
            } catch (IOException e) {
                e.printStackTrace();
            }
            network.close();
        });
        openAuthDialog();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatClient.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private void openAuthDialog() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ChatClient.class.getResource("authServicePanel.fxml"));
        AnchorPane parent = loader.load();


        authDialogStage = new Stage();
        authDialogStage.initModality(Modality.WINDOW_MODAL);
        authDialogStage.initOwner(primaryStage);

        AuthController authController = loader.getController();
        authController.setNetwork(network);

        authDialogStage.setScene(new Scene(parent));
        authDialogStage.show();

    }

    public static void showNetworkError(String errorDetails, String errorTitle, Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        if (dialogStage != null) {
            alert.initOwner(dialogStage);
        }
        alert.setTitle("Network error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorDetails);
        alert.showAndWait();
    }

    public ChatClientState getState() {
        return state;
    }

    public void setState(ChatClientState state) {
        this.state = state;
    }

    public static void main(String[] args) {
        launch();
    }

    public void activeChatDialog(String nickname) {
        primaryStage.setTitle(nickname);
        clientNickname = nickname;
        state = ChatClientState.CHAT;
        authDialogStage.close();
        primaryStage.show();
        controller.getChatLog().appendText(Log.displayLog(network.getNickname()));
        controller.getTextField().requestFocus();
    }


    }
