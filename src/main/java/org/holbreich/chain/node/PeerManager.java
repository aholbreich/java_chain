package org.holbreich.chain.node;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO make this a Service
public interface PeerManager {

    static final Logger log = LoggerFactory.getLogger(PeerManager.class);
    static List<ChainClient> peerNodes = new ArrayList<>();


    public static void addPeer(String host, int port) {
        // log.info("Attempt to add new peer [" + host + ":" + port + " ]");

        ChainClient client = new ChainClient(new InetSocketAddress(host, port));
        if (peerNodes.contains(client)) {
            log.warn("Peer already exists: " + host + ":" + port);
            return;
        }

        if (client.askLatestHash() != null) {
            peerNodes.add(client); // check on functionality
            log.info("New peer added: " + host + ":" + port);
            client.registerListener(host, port);
        }
    }
}
