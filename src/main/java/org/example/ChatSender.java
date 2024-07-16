//Part of the chat Application. Need to run ChatReceiver before running this file so that message can be sent and received.
//Currently working withing private IPFS connected to same local network(using local IP).
//Need to add IPFS ping before sending to check if the receiver is ready to accept.
//Points to develop - need to IPFS ping before sending any message
//                  - Need to be able to setup and use the private IPFS network and send to a node in public network
//                    (Need to explore how the private IPFS node connects to another node in network.)
//                    (After running daemon, by running IPFS id need to know what the node announces about the IP and peer ID and how it is selecting)


package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatSender {
    private final IPFS ipfs;
    private final Socket socket;
    private final PrintWriter out;

    public ChatSender(String multiAddr, String receiverHost, int receiverPort) throws IOException {
        this.ipfs = new IPFS(multiAddr);
        this.socket = new Socket(receiverHost, receiverPort);
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) throws IOException {
        String hash = uploadMessageToIPFS(message);
        sendHashToReceiver(hash);
    }

    private String uploadMessageToIPFS(String message) throws IOException {
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper(message.getBytes());
        MerkleNode addResult = ipfs.add(file).get(0);
        return addResult.hash.toString();
    }

    private void sendHashToReceiver(String hash) {
        out.println(hash);
    }

    public static void main(String[] args) throws IOException {
        String receiver_IP = "127.0.0.1";
        ChatSender sender = new ChatSender("/ip4/127.0.0.1/tcp/5001", receiver_IP, 5000);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter messages to send (type 'exit' to quit):");

        while (true) {
            System.out.print("Message: ");
            String message = scanner.nextLine();
            if (message.equalsIgnoreCase("exit")) {
                break;
            }
            sender.sendMessage(message);
        }

        scanner.close();
        sender.socket.close();
    }
}
