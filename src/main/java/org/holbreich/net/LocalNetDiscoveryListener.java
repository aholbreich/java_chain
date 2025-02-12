package org.holbreich.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

import org.holbreich.chain.node.ChainClient;
import org.holbreich.chain.node.PeerManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LocalNetDiscoveryListener extends Thread implements Discovery {

    private static final Logger log = LoggerFactory.getLogger(LocalNetDiscoveryListener.class);
    private boolean running = true;

    public void run() {
        log.info("Listening for Discover broadcas on {}", DISCOVERY_BROADCAST_PORT);
        listenForNodes();
    }

    public void listenForNodes() {

        try (DatagramSocket socket = new DatagramSocket(DISCOVERY_BROADCAST_PORT)) {
            socket.setBroadcast(true);
            byte[] buffer = new byte[256];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (running) {
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if (msg.startsWith("DISCOVER_NODE,")) {
                    String[] parts = msg.split(",");
                    String host = parts[1];
                    String port = parts[2];
                    log.info("Discovered node at host {} port {}", host, port);
                    PeerManager.addPeer(host, Integer.parseInt(port));
                }
            }
          
        } catch (IOException e) {
            if (e.getMessage().contains("Address already in use")){
                // Another instance is already running on same port. This is fine. So another instance is in chared to listen to our anouncement.
                log.info("Seem that another instance is already running on same machin/port. That's ok.");
            }
            log.error("Error listening for nodes: {}", e.getMessage());
        }
        log.info("Discovery listener stopped");
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
