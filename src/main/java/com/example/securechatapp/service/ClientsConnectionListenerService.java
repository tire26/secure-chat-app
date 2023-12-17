package com.example.securechatapp.service;

import com.example.securechatapp.model.User;
import javafx.application.Platform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientsConnectionListenerService {

    private ConnectedClientsStorageService connectedClientsStorageService;
    private TcpSenderService tcpSenderService;

    @Scheduled(fixedDelay = 1000)
    public void checkConnectionWithClients() {
        List<User> allClients = connectedClientsStorageService.getAll();
        for (User client : allClients) {
            if (!isAvailable(client)) {
                Platform.runLater(() -> connectedClientsStorageService.delete(client));
            }
        }
    }

    private boolean isAvailable(User client) {
        return tcpSenderService.sendMessage(client);
    }

    @Autowired
    public void setConnectedClientsStorageService(ConnectedClientsStorageService connectedClientsStorageService) {
        this.connectedClientsStorageService = connectedClientsStorageService;
    }

    @Autowired
    public void setTcpSenderService(TcpSenderService tcpSenderService) {
        this.tcpSenderService = tcpSenderService;
    }
}
