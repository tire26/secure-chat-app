package com.example.securechatapp.service;

import com.example.securechatapp.model.ChatHistory;
import com.example.securechatapp.model.User;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

    @Getter
    private Map<User, ChatHistory> userChatMap;
    private MessageService messageService;
    private UdpSenderService udpSender;
    private String nickname;
    @Autowired
    public ChatService(MessageService messageService,
                       UdpSenderService udpSender, String nickname) {
        this.udpSender = udpSender;
        this.nickname = nickname;
        userChatMap = new HashMap<>();
        this.messageService = messageService;
        this.udpSender.broadcastPublicKey();
    }

    public void createChat(User conversationUser) {
        ChatHistory newChatHistory = new ChatHistory();
        userChatMap.put(conversationUser, newChatHistory);
    }

    public String encryptAndSendMessage(String message, User conversationUser) {
        byte[] encryptMessage = messageService.encryptMessage(message, conversationUser.getPublicKey());
        messageService.sendMessage(encryptMessage, conversationUser);

        ChatHistory chatHistory = userChatMap.get(conversationUser);
        String encryptedMsgString = new String(encryptMessage, StandardCharsets.UTF_8);
        chatHistory.add(message, nickname);
        return encryptedMsgString;
    }

    public String decryptMessage(byte[] message) {
        String decryptedMessage = messageService.decryptMessage(message);
        return decryptedMessage;
    }

}
