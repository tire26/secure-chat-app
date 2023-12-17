package com.example.securechatapp.config;

import com.example.securechatapp.model.Nickname;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CurrentUserNicknameConfig {

    @Bean
    public Nickname nickname() {
        return new Nickname();
    }
}
