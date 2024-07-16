package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multiaddr.MultiAddress;

import java.io.IOException;

public class IPFSExample {
    public static void main(String[] args) throws IOException {
        // Connect to your local IPFS node
        IPFS ipfs = new IPFS(new MultiAddress("/ip4/127.0.0.1/tcp/5001"));

        // Add a file to IPFS
        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(new java.io.File("D:\\Internship\\IPFS-private\\PrivateIPFS\\src\\main\\java\\files\\file.txt"));
        MerkleNode addResult = ipfs.add(file).getFirst();
        System.out.println(STR."Added file hash: \{addResult.hash}");

        // Retrieve a file from IPFS
        byte[] data = ipfs.cat(addResult.hash);
        System.out.println(STR."Retrieved file content: \{new String(data)}");
    }
}
