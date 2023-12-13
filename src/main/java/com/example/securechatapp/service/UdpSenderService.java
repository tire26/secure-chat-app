package com.example.securechatapp.service;

import com.example.securechatapp.model.User;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.management.ManagementFactory;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.security.PublicKey;

@Service
public class UdpSenderService extends UdpWorkerAbstract {

    private String nickname;

    @Autowired
    public UdpSenderService(KeyGeneratorService keyGeneratorService, String nickname) {
        this.nickname = nickname;
        this.keyGeneratorService = keyGeneratorService;
        this.logger = LoggerFactory.getLogger(ManagementFactory.class.getName());
    }

    public void broadcastPublicKey() {
        sendIdentity();
    }

    protected void sendIdentity() {
        InetAddress localIp = getLocalIp();

        try {
            PublicKey publicKey = keyGeneratorService.getPublicKey();

            User currUser = User.builder()
                    .publicKey(publicKey)
                    .inetAddress(localIp)
                    .nickname(nickname)
                    .build();

            final ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            final ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(currUser);
            byte[] sendData = baos.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcastInetAddress, port);

            datagramSocket.send(sendPacket);

            logger.atInfo().log("Отправлен широковещательный запрос по ip: " + broadcastInetAddress.getHostAddress());
        } catch (IOException e) {
            logger.atError().log(e.getMessage());
        }
    }
}
