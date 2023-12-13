package com.example.securechatapp.service;

import com.example.securechatapp.controller.ChatController;
import com.example.securechatapp.model.User;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.*;

@Component
public class ConnectedClientsService {

    private final List<User> connectedClients;

    public ConnectedClientsService() {
        connectedClients = new ArrayList<>();
    }

    public boolean contains(String nickname) {
        return connectedClients.stream().anyMatch(user -> user.getNickname().equals(nickname));
    }

    public Optional<User> getUserByInetAddress(InetAddress inetAddress) {
        return connectedClients.stream().filter(user -> user.getInetAddress().equals(inetAddress)).findFirst();
    }

    public void add(User conversationUser) {
        connectedClients.add(conversationUser);
    }

    public Map<String, User> getUserMap() {
        Map<String, User> userMap = new HashMap<>();
        for (User user : connectedClients) {
            userMap.put(user.getNickname(), user);
        }
        return userMap;
    }
}
