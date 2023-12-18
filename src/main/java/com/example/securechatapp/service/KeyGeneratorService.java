// KeyGeneratorService.java

package com.example.securechatapp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.security.*;
import java.util.Base64;


@Service
public class KeyGeneratorService {

    private final Logger logger;
    private KeyPair keyPair;

    public KeyGeneratorService() {
        logger = LoggerFactory.getLogger(ManagementFactory.class.getName());
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            logger.atError().log(e.getMessage());
        }
    }

    public PublicKey getPublicKey() {
        if (keyPair != null) {
            return keyPair.getPublic();
        }
        return null;
    }

    public PrivateKey getPrivateKey() {
        if (keyPair != null) {
            return keyPair.getPrivate();
        }
        return null;
    }

    public String getFormattedPublicKey() {
        if (keyPair != null) {
            PublicKey publicKey = keyPair.getPublic();
            return Base64.getEncoder().encodeToString(publicKey.getEncoded());
        }
        return null;
    }
}
