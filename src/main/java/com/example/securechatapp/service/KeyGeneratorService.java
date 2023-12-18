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
            String formattedKey = formatPEM(publicKey.getEncoded(), "PUBLIC");
            return formattedKey;
        }
        return null;
    }

    public String getFormattedPrivateKey() {
        if (keyPair != null) {
            PrivateKey privateKey = keyPair.getPrivate();
            String formattedKey = formatPEM(privateKey.getEncoded(), "PRIVATE");
            return formattedKey;
        }
        return null;
    }

    private String formatPEM(byte[] keyBytes, String keyMod) {
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);

        StringBuilder formattedKeyBuilder = new StringBuilder("-----BEGIN RSA " + keyMod + " KEY-----\n");
        for (int i = 0; i < base64Key.length(); i++) {
            formattedKeyBuilder.append(base64Key.charAt(i));
            if ((i + 1) % 40 == 0) {
                formattedKeyBuilder.append("\n");
            }
        }
        formattedKeyBuilder.append("\n-----END RSA ").append(keyMod).append(" KEY-----");
        return formattedKeyBuilder.toString();
    }
}
