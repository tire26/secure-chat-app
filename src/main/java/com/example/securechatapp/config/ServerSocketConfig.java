package com.example.securechatapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.ServerSocket;

@Configuration
public class ServerSocketConfig {

    @Value("${networkmanager.tcp-port}")
    private int tcpSocketPort;

    @Bean
    public ServerSocket serverSocket() throws IOException {
        return new ServerSocket(tcpSocketPort);
    }
}
