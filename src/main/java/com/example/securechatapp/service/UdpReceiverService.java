package com.example.securechatapp.service;

import com.example.securechatapp.controller.ChatController;
import com.example.securechatapp.model.User;
import jakarta.annotation.PostConstruct;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.management.ManagementFactory;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class UdpReceiverService extends UdpWorkerAbstract {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private ConnectedClientsService connectedClientsService;
    private ChatController chatController;
    private DatagramSocket datagramSocket;
    private UdpSenderService udpSenderService;
    private String nickname;

    @Autowired
    public UdpReceiverService(ConnectedClientsService connectedClientsService,
                              ChatController chatController,
                              KeyGeneratorService keyGeneratorService, UdpSenderService udpSenderService, String nickname) {
        this.udpSenderService = udpSenderService;
        this.nickname = nickname;
        logger = LoggerFactory.getLogger(ManagementFactory.class.getName());
        this.keyGeneratorService = keyGeneratorService;
        this.chatController = chatController;
        this.connectedClientsService = connectedClientsService;
    }

    @PostConstruct
    public void startUdpListener() {
        executorService.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            try {
                while (true) {
                    if (!datagramSocket.isClosed()) {
                        byte[] receiveData = new byte[1024];
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        datagramSocket.receive(receivePacket);

                        byte[] data = receivePacket.getData();
                        ByteArrayInputStream bais = new ByteArrayInputStream(data);
                        ObjectInputStream ois = new ObjectInputStream(bais);
                        Object o = ois.readObject();

                        if (o instanceof User) {
                            addNewUserInNetwork((User) o);
                        }
                    } else {
                        break;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                logger.atError().log(e.getMessage());
            }
        });
    }

    private void addNewUserInNetwork(User o) {
        User user = o;
        if (!connectedClientsService.contains(user.getNickname()) && !user.getNickname().matches(nickname)) {
            connectedClientsService.add(user);
            chatController.newUser();

            udpSenderService.sendIdentity();

            logger.atInfo().log("Получен пакет с пользователем " + o.getNickname());
        }
    }

    @Autowired
    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }
}
