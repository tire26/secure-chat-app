package com.example.securechatapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.DatagramSocket;
import java.net.SocketException;

@Configuration
public class DatagramSocketConfig {

    @Value("${networkmanager.udp-port}")
    protected int port;

    @Bean
    public DatagramSocket datagramSocket() throws SocketException {
        return new DatagramSocket(port);
    }
}
