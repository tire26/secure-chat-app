package com.example.securechatapp.service;

import com.example.securechatapp.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.security.InvalidKeyException;
import java.security.PublicKey;

@Service
public class MessageService {

    private MessageCryptService messageCryptService;
    private Logger logger;
    private TcpSenderService tcpSenderService;
    private KeyGeneratorService keyGeneratorService;

    @Autowired
    public MessageService(MessageCryptService messageCryptService,
                          TcpSenderService tcpSenderService,
                          KeyGeneratorService keyGeneratorService) {
        logger = LoggerFactory.getLogger(ManagementFactory.class.getName());
        this.messageCryptService = messageCryptService;
        this.tcpSenderService = tcpSenderService;
        this.keyGeneratorService = keyGeneratorService;
    }

    public byte[] encryptMessage(String message, PublicKey publicKey) {
        try {
            return messageCryptService.encryptMessage(message, publicKey);
        } catch (InvalidKeyException e) {
            logger.atError().log(e.getMessage());
        }
        return new byte[0];
    }

    public void sendMessage(byte[] encryptedMessages, User conversationUser) {
        tcpSenderService.sendEncryptedMessage(encryptedMessages, conversationUser);
    }


    public String decryptMessage(byte[] data) {
        String decryptMessage = "";
        try {
            decryptMessage = messageCryptService.decryptMessage(data, keyGeneratorService.getPrivateKey());
            return decryptMessage;
        } catch (InvalidKeyException e) {
            logger.atError().log(e.getMessage());
        }
        return decryptMessage;
    }
}
