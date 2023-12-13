package com.example.securechatapp.config;

import com.example.securechatapp.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class CurrentUserNicknameConfig {

    @Bean
    public String nickname() {
        return generateNickname();
    }

    public static String generateNickname() {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_";
        int length = 8;
        Random random = new Random();
        StringBuilder nickname = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            char randomChar = characters.charAt(index);
            nickname.append(randomChar);
        }

        return nickname.toString();
    }
}
