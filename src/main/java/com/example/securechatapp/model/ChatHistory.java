package com.example.securechatapp.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatHistory {

    List<String> usersNicknames;
    List<String> messages;

    public ChatHistory() {
        usersNicknames = new ArrayList<>();
        messages = new ArrayList<>();
    }

    public void add(String userMessage, String nickname) {
        usersNicknames.add(nickname);
        messages.add(userMessage);
    }

    public String getChatHistory() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < messages.size(); i++) {
            stringBuilder.append(usersNicknames.get(i)).append(" : ").append(messages.get(i)).append("\n");
        }
        return stringBuilder.toString();
    }
}
