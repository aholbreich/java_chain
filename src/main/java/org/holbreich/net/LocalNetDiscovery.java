package org.holbreich.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LocalNetDiscovery  implements Discovery {

    private static final Logger log = LoggerFactory.getLogger(LocalNetDiscovery.class);

    public void broadcastPresence(int port) throws UnknownHostException {

        String host = InetAddress.getLocalHost().getHostAddress();

        log.info("Broadcasting presence on {} port: {}",host, port);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            byte[] messageBytes = getPacketMessage(host,port).getBytes();
            DatagramPacket packet = new DatagramPacket(messageBytes, messageBytes.length,
                    InetAddress.getByName("255.255.255.255"), DISCOVERY_BROADCAST_PORT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getPacketMessage(String host, int port) throws UnknownHostException {
        return "DISCOVER_NODE," + host + "," + port;

    }

}
