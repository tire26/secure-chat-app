package com.example.securechatapp.service;

import com.example.securechatapp.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.Socket;

@Service
public class TcpSenderService {

    @Value("${networkmanager.tcp-port}")
    private int tcpSocketPort;

    public void sendEncryptedMessage(byte[] bytes, User conversationUser) {
        try (Socket clientSocket = new Socket(conversationUser.getInetAddress(), tcpSocketPort)) {
            clientSocket.getOutputStream().write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
