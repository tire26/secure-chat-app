package com.example.securechatapp.controller;

import com.example.securechatapp.model.ChatHistory;
import com.example.securechatapp.model.User;
import com.example.securechatapp.service.ChatService;
import com.example.securechatapp.service.ConnectedClientsStorageService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.NoArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@FxmlView("/main-page.fxml")
@NoArgsConstructor
public class ChatController {

    public ListView<String> availableUsersListView;
    public VBox userPanel;
    public Text chattingUserNick;
    private ChatService chatService;
    private ConnectedClientsStorageService connectedClientsStorageService;
    private User conversationUser;
    private Map<String, User> userMap;
    private ChatHistory currentChatHistory;

    @FXML
    private TextArea chatHistoryTextArea;
    @FXML
    private TextArea inputText;
    @FXML
    private TextArea encryptedMessageTextArea;

    @FXML
    public void initialize() {
        chatService.startUdpSession();
        updateUsersPanel();
        userMap = connectedClientsStorageService.getUserMap();
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

    public void newUser(User conversationUser) {
        Platform.runLater(() -> {
            chatService.createChat(conversationUser);
            addUserInScene();
            availableUsersListView.requestLayout();
        });
    }

    private void addUserInScene() {
        userMap = connectedClientsStorageService.getUserMap();
        availableUsersListView.getItems().clear();

        addUsersInScene(userMap);
    }

    public synchronized void updateUsersPanel() {
        userMap = connectedClientsStorageService.getUserMap();
        availableUsersListView.getItems().clear();
        addUsersInScene(userMap);
    }

    public void clearChat(User client) {
        if (client.equals(conversationUser)) {
            clearInputArea();
            currentChatHistory = null;
            chatHistoryTextArea.clear();
            chattingUserNick.setText("");
        }
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
    public void encryptAndSend() {
        String message = inputText.getText();
        String encryptedMessage = chatService.encryptAndSendMessage(message, conversationUser);
        encryptedMessageTextArea.setText(encryptedMessage);
        displayChatHistory(conversationUser);
    }

    @FXML
    public void clearInputArea() {
        encryptedMessageTextArea.clear();
        inputText.clear();
    }

    @Autowired
    public void setChatService(ChatService chatService) {
        this.chatService = chatService;
    }

    @Autowired
    public void setConnectedClientsStorageService(ConnectedClientsStorageService connectedClientsStorageService) {
        this.connectedClientsStorageService = connectedClientsStorageService;
    }
}
