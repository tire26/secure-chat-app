package com.example.securechatapp.controller;

import com.example.securechatapp.model.ChatHistory;
import com.example.securechatapp.model.User;
import com.example.securechatapp.service.ChatService;
import com.example.securechatapp.service.ConnectedClientsService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@FxmlView("/main-page.fxml")
public class ChatController {

    public ListView<String> availableUsersListView;
    public VBox userPanel;
    public Text chattingUserNick;
    private ChatService chatService;
    private ConnectedClientsService connectedClientsService;
    private User conversationUser;
    private Map<String, User> userMap;
    private ChatHistory currentChatHistory;

    @FXML
    private TextArea chatHistoryTextArea;
    @FXML
    private TextArea inputText;
    @FXML
    private TextArea encryptedMessageTextArea;

    @Autowired
    public ChatController(ChatService chatService, ConnectedClientsService connectedClientsService) {
        this.chatService = chatService;
        this.connectedClientsService = connectedClientsService;
    }

    @FXML
    public void initialize() {
        updateUsersPanel();
        userMap = connectedClientsService.getUserMap();
        availableUsersListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                String nickname = availableUsersListView.getSelectionModel().getSelectedItem();
                User chattingUSer = userMap.get(nickname);
                currentChatHistory = chatService.getUserChatMap().get(chattingUSer);
                chattingUserNick.setText(nickname);
                displayChatHistory(userMap.get(nickname));
            }
        });
    }

    public void receiveMessage(byte[] encryptedData, User user) {
        String message = chatService.decryptMessage(encryptedData);
        ChatHistory currChatHistory = chatService.getUserChatMap().get(user);
        currChatHistory.add(message, user.getNickname());
        if (Objects.equals(currChatHistory, this.currentChatHistory)) {
            displayChatHistory(user);
        }
    }

    public void newUser() {
        Platform.runLater(() -> {
            addUserInScene();
            availableUsersListView.requestLayout();
        });
    }

    private void addUserInScene() {
        userMap = connectedClientsService.getUserMap();
        availableUsersListView.getItems().clear();

        addUsersInScene(userMap);
    }

    private void updateUsersPanel() {
        userMap = connectedClientsService.getUserMap();
        availableUsersListView.getItems().clear();
        addUsersInScene(userMap);
    }

    private void addUsersInScene(Map<String, User> userList) {
        for (User u : userList.values()) {
            availableUsersListView.getItems().add(u.getNickname());
        }
    }

    private void displayChatHistory(User user) {
        Platform.runLater(() -> {
            conversationUser = user;
            String chatHistory = chatService.getUserChatMap().get(user).getChatHistory();
            chatHistoryTextArea.setText(chatHistory);
            chatHistoryTextArea.requestLayout();
        });
    }

    @FXML
    public void encryptAndSend(ActionEvent actionEvent) {
        String message = inputText.getText();
        String encryptedMessage = chatService.encryptAndSendMessage(message, conversationUser);
        encryptedMessageTextArea.setText(encryptedMessage);
        displayChatHistory(conversationUser);
    }

    @FXML
    public void clear(ActionEvent actionEvent) {
        encryptedMessageTextArea.clear();
        inputText.clear();
    }
}
