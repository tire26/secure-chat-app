package com.example.securechatapp;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecureChatAppApplication {

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }
}
