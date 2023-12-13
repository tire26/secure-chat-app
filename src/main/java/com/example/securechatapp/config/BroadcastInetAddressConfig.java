package com.example.securechatapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@Configuration
public class BroadcastInetAddressConfig {

    @Value("${networkmanager.broadcast-network}")
    private String broadcastIp;

    @Bean
    public InetAddress broadcastInetAddress() throws SocketException {
        return getInetAddress(broadcastIp);
    }

    private InetAddress getInetAddress(String ip) throws SocketException {
        List<InetAddress> inetAddresses = listAllBroadcastAddresses();
        for (InetAddress inetAddress : inetAddresses) {
            if (inetAddress.toString().equals(ip)) {
                return inetAddress;
            }
        }
        return null;
    }

    private List<InetAddress> listAllBroadcastAddresses() throws SocketException {
        List<InetAddress> broadcastList = new ArrayList<>();
        Enumeration<NetworkInterface> interfaces
                = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();

            if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                continue;
            }

            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast != null) {
                    broadcastList.add(broadcast);
                }
            }
        }
        return broadcastList;
    }
}
