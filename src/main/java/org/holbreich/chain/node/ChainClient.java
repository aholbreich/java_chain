package org.holbreich.chain.node;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.holbreich.chain.Block;

public class ChainClient {

    private InetSocketAddress peerAddress;
    private static final Logger log = LoggerFactory.getLogger(ChainClient.class);

    public ChainClient(InetSocketAddress peerAddress) {
        this.peerAddress = peerAddress;
    }

    /**
     * Asks peer Node for last known block hash
     * 
     * @return
     */
    public String askLatestHash() {
        // TODO Nothig is robust here

        try (Socket socket = new Socket(peerAddress.getHostName(), peerAddress.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject("LATEST_HASH");
            String latestHash = (String) in.readObject();
            log.debug("Retreived lastes hash: {} ", latestHash);
            // Read and print the server's response (expected: "ACK")

            return latestHash;
        } catch (IOException | ClassNotFoundException e) {
            log.debug("Error communicating with the server {}", e.getMessage());
            return null;
        }
    }

    /**
     * Sends missing blocks to a peer
     * 
     * @param blocks
     */
    public void sendBlocks(List<Block> blocks) {

        try (Socket socket = new Socket(peerAddress.getHostName(), peerAddress.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            log.info("Sending {} blocks to {}", blocks.size(), peerAddress.toString());
            out.writeObject(blocks);

        } catch (IOException e) {
            log.debug("Error communicating with the server {}", e.getMessage());
        }
    }

    /**
     * Registers a give Socket Adress as a new peer
     * 
     * @param host
     * @param port
     */
    public void registerListener(String host, int port) {

        try (Socket socket = new Socket(peerAddress.getHostName(), peerAddress.getPort());
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            String socketAddr = "PEER," + host + "," + port;
            log.info("Trying to register new peer {}", socketAddr);
            out.writeObject(socketAddr);

        } catch (IOException e) {
            log.debug("Error communicating with the server {}", e.getMessage());
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((peerAddress == null) ? 0 : peerAddress.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChainClient other = (ChainClient) obj;
        if (peerAddress == null) {
            if (other.peerAddress != null)
                return false;
        } else if (!peerAddress.equals(other.peerAddress))
            return false;
        return true;
    }

}
