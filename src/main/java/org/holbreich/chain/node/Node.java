package org.holbreich.chain.node;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.holbreich.chain.Block;
import org.holbreich.chain.Blockchain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Node implements PeerManager {

    private static final Logger log = LoggerFactory.getLogger(Node.class);

    private static final int DEFAULT_PORT = 5000; // Port for communication
    private static Blockchain chain = new Blockchain();

    private static SimpleServerThread serverThread;

    public static void main(String[] args) throws InterruptedException {

        serverThread = new SimpleServerThread(getPort(args), chain);
        serverThread.start();

        // Simulating block creation and broadcasting to peers
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter transaction data (or 'exit' to stop): ");
            String data = scanner.nextLine();
            if (data.equalsIgnoreCase("exit"))
                break;

            // Expected input format: join host|port
            // On my linux it's eg. "join ::1_5000"
            if (data.startsWith("join")) {
                String[] parts = data.substring(4).split("_");
                if (parts.length == 2) {
                    PeerManager.addPeer(parts[0].trim(), Integer.parseInt(parts[1]));
                    continue;
                }
            }

            chain.addBlock(data);
            broadcastBlockchain();
        }
        scanner.close();
        log.warn("Stoping Server thread");
        serverThread.setRunning(false);
        serverThread.join(200);
        System.exit(0);
    }

    private static int getPort(String[] args) {
        // Check if a port argument is provided
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        return port;
    }

    public static void broadcastBlockchain() {
        log.info("Broadcasting blockchain to all peers");
        for (ChainClient peer : peerNodes) {
            String latestHash = peer.askLatestHash();
            List<Block> missingNodes = chain.getMissingNodes(latestHash);
            if (missingNodes != null) {
                peer.sendBlocks(missingNodes);
            }
        }
    }


}
