package com.example.securechatapp.service;

import com.example.securechatapp.model.ChatHistory;
import com.example.securechatapp.model.Nickname;
import com.example.securechatapp.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
    private Nickname nickname;
    private UdpReceiverService udpReceiverService;

    public ChatService() {
        userChatMap = new HashMap<>();
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
        chatHistory.add(message, nickname.getNickname());
        return encryptedMsgString;
    }

    public String decryptMessage(byte[] message) {
        String decryptedMessage = messageService.decryptMessage(message);
        return decryptedMessage;
    }

    public void startUdpSession() {
        this.udpSender.broadcastPublicKey();
        this.udpReceiverService.startUdpListener();
    }

    @Autowired
    public void setUdpReceiverService(@Lazy UdpReceiverService udpReceiverService) {
        this.udpReceiverService = udpReceiverService;
    }

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Autowired
    public void setUdpSender(UdpSenderService udpSender) {
        this.udpSender = udpSender;
    }

    @Autowired
    public void setNickname(Nickname nickname) {
        this.nickname = nickname;
    }
}
