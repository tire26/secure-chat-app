package com.example.securechatapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

@Service
public class MessageCryptService {

    private final Cipher encryptCipher;
    private final Logger logger;

    public MessageCryptService() throws NoSuchPaddingException, NoSuchAlgorithmException {
        encryptCipher = Cipher.getInstance("RSA");
        logger = LoggerFactory.getLogger(ManagementFactory.class.getName());
    }

    public byte[] encryptMessage(String message, PublicKey publicKey) throws InvalidKeyException {
        encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] secretMessageBytes = message.getBytes(StandardCharsets.UTF_8);
        try {
            byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
            return encryptedMessageBytes;
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            logger.atError().log(e.getMessage());
            return new byte[0];
        }
    }

    public String decryptMessage(byte[] encryptedMessage, PrivateKey privateKey) throws InvalidKeyException {
        encryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedMessageBytes = new byte[0];
        try {
            decryptedMessageBytes = encryptCipher.doFinal(encryptedMessage);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            logger.atError().log(e.getMessage());
        }
        String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);
        return decryptedMessage;
    }
}
