package com.example.securechatapp.service;

import com.example.securechatapp.model.BroadcastInetAddress;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.net.*;

public abstract class UdpWorkerAbstract {

    @Value("${networkmanager.udp-port}")
    protected int port;
    protected Logger logger;
    protected KeyGeneratorService keyGeneratorService;
    protected DatagramSocket datagramSocket;
    protected BroadcastInetAddress broadcastInetAddress;



    protected InetAddress getLocalIp() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("google.com", 80));
            return socket.getLocalAddress();
        } catch (IOException e) {
            logger.atError().log(e.getMessage());
            return null;
        }
    }

    @Autowired
    public void setDatagramSocket(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    @Autowired
    public void setBroadcastInetAddress(BroadcastInetAddress inetAddress) {
        this.broadcastInetAddress = inetAddress;
    }
}
