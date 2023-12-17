package com.example.securechatapp.model;

import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;

@Getter
@Setter
public class BroadcastInetAddress {

    public BroadcastInetAddress() {
    }

    private InetAddress broadcastInetAddress;
}
