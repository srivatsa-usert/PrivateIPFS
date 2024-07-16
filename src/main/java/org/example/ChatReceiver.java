//Part of the chat Application, need to be run the ChatReceiver to recieve a message before sending a message.
//Currently working withing private IPFS connected to same local network(using local IP).
//Receiving the message through the private IPFS network with the swarm key.
//Points to develop -
//                  - Need to be able to setup and use the private IPFS network and send to a node in public network

package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.multihash.Multihash;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatReceiver {
    private final IPFS ipfs;
    private final ServerSocket serverSocket;

    public ChatReceiver(String multiAddr, int port) throws IOException {
        this.ipfs = new IPFS(multiAddr);
        this.serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String hash;
        while ((hash = in.readLine()) != null) {
            String message = downloadMessageFromIPFS(hash);
            displayMessage(message);
        }
    }

    private String downloadMessageFromIPFS(String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        byte[] fileContents = ipfs.cat(filePointer);
        return new String(fileContents);
    }

    private void displayMessage(String message) {
        System.out.println("Received message: " + message);
    }

    public static void main(String[] args) throws IOException {
        ChatReceiver receiver = new ChatReceiver("/ip4/127.0.0.1/tcp/5001", 5000);
        receiver.start();
    }
}
