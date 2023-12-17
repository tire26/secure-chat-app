package com.example.securechatapp.service;

import com.example.securechatapp.controller.ChatController;
import com.example.securechatapp.model.Nickname;
import com.example.securechatapp.model.User;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class TcpReceiverService {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Logger logger;
    private ConnectedClientsStorageService connectedClientsStorageService;
    private ChatController chatController;
    private ServerSocket serverSocket;
    private Nickname nickname;

    public TcpReceiverService(ConnectedClientsStorageService connectedClientsStorageService,
                              ChatController chatController, ServerSocket serverSocket,
                              Nickname nickname) {
        this.connectedClientsStorageService = connectedClientsStorageService;
        this.chatController = chatController;
        this.serverSocket = serverSocket;
        this.nickname = nickname;
        logger = LoggerFactory.getLogger(ManagementFactory.class.getName());

    }

    @PostConstruct
    public void startTcpListener() {
        executorService.execute(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                while (true) {
                    if (!serverSocket.isClosed()) {
                        Socket socket = serverSocket.accept();
                        byte[] data = socket.getInputStream().readAllBytes();
                        if (data.length > 0) {
                            receiveEncryptedMessage(socket.getInetAddress(), data);
                        }
                    } else {
                        break;
                    }
                }
            } catch (IOException e) {
                logger.atError().log(e.getMessage());
            }
        });
    }

    private void receiveEncryptedMessage(InetAddress inetAddress, byte[] data) {
        Optional<User> optionalUser = connectedClientsStorageService.getUserByInetAddress(inetAddress);
        User user = optionalUser.orElse(null);
        if (!Objects.equals(user, null) && !user.getNickname().matches(nickname.getNickname())) {
            chatController.receiveMessage(data, user);
        }
    }
}
