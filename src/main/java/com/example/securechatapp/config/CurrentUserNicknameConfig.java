package com.example.securechatapp.config;

import com.example.securechatapp.model.Nickname;
import com.example.securechatapp.model.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class CurrentUserNicknameConfig {

    @Bean
    public Nickname nickname() {
        return new Nickname();
    }
}
