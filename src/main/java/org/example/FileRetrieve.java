

package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.multiaddr.MultiAddress;
import io.ipfs.multihash.Multihash;

import java.io.FileOutputStream;

import java.io.IOException;

public class FileRetrieve {

    public static void main(String[] args) throws IOException {
        // Connect to IPFS daemon
        IPFS ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.2/tcp/5001"));

        // Hash of the file sent by the sender
        String fileHash = "QmPwHJBUQpz8w2VvztY87b8wemyW7ePgo5wDLShQugN4eZ"; // Replace this with the hash received from the sender
        Multihash fileHashMultiHash = Multihash.fromBase58(fileHash);

        // Retrieve the file
        byte[] retrievedData = ipfs.cat(fileHashMultiHash);
        System.out.println("File retrieved from IPFS.");

        saveToFile(retrievedData, "D:\\Internship\\IPFS-private\\PrivateIPFS\\src\\main\\java\\files\\1,png");
    }

    private static void saveToFile(byte[] data, String filePath) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(filePath);
        outputStream.write(data);
        outputStream.close();
        System.out.println("File saved to: " + filePath);
    }
}
