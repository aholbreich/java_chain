package org.holbreich.chain.node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.holbreich.chain.Block;
import org.holbreich.chain.Blockchain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleServerThread extends Thread {

    private static final Logger log = LoggerFactory.getLogger(SimpleServerThread.class);
    private int port;
    private Blockchain blockchain;
    private volatile boolean running = true;

    public SimpleServerThread(int port, Blockchain blockchain) {
        this.port = port;
        this.blockchain = blockchain;
    }

    public void run() {

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Node started. Listening on inetAddress: {} ,  local socket: {}, port: {}",
                    serverSocket.getInetAddress(), serverSocket.getLocalSocketAddress(), port);

            while (running) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }
            log.info("Server thread stopped");
        } catch (IOException e) {
            log.error("Error starting/running server: {}", e.getMessage());
        }
    }

    /**
     * Handles incoming client requests
     * 
     * @param socket
     */
    private void handleClient(Socket socket) {

        try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {

            Object received = in.readObject();
            log.debug("Received: {}", received);
            // Requesting the latest block hash
            if (received instanceof String && ((String) received).equals("LATEST_HASH")) {
                out.writeObject(blockchain.getBlock(blockchain.getSize() - 1).getHash());
            }

            // Receiving missing blocks
            else if (received instanceof List) {
              
                List<Block> receivedBlocks = (List<Block>) received;
                log.debug("Received new Blocks, size: {}", receivedBlocks.size());
                log.warn("Not implemented: addAllValid");
                // blockchain.addAllValid(receivedBlocks);

            }

            else if (received instanceof String && ((String) received).startsWith("PEER")) {
                log.debug("New PEER announcement: {}", received);
                String msg = (String) received;
                if (msg.startsWith("PEER,")) {
                    String[] parts = msg.split(",");
                    String host = parts[1];
                    String port = parts[2];
                    log.info("Discovered node at host {} port {}", host, port);
                    PeerManager.peerNodes.add(new ChainClient(new InetSocketAddress(host, Integer.parseInt(port))));
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            log.error("Error handling client request: {}", e.getMessage());
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
