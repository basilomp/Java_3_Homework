package org.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.client.ChatClient;
import org.client.Network;

import java.io.IOException;

public class AuthController {

    private static final String AUTH_CMD = "auth"; // "/auth login password"

    private Network network;

    @FXML
    public TextField loginField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public void executeAuth(ActionEvent actionEvent) {
        String login = loginField.getText();
        String password = passwordField.getText();

        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            ChatClient.showNetworkError("Enter login and password", "Validation error", null);
            return;
        }

        try {
            network.sendAuthMessage(login, password);
        } catch (IOException e) {
            ChatClient.showNetworkError(e.getMessage(), "Auth error", null);

            e.printStackTrace();
        }


    }

    public void setNetwork(Network network) {
        this.network = network;
    }
}
