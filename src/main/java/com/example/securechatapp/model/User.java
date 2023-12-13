package com.example.securechatapp.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.net.InetAddress;
import java.security.PublicKey;
import java.util.Random;

@Data
@Builder
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nickname;
    private InetAddress inetAddress;
    private PublicKey publicKey;

}
