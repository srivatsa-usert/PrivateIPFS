package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.multihash.Multihash;

import java.io.IOException;

public class RetrieveFileFromNode2 {

    public static void main(String[] args) throws IOException {
        // Initialize IPFS client for Node 2
        IPFS ipfsNode2 = new IPFS("/ip4/127.0.0.2/tcp/5001");

        // Hash of the file added from Node 1
        Multihash fileHashFromNode1 = Multihash.fromBase58("QmQvDGv4kQzLy667GsPRe4W71v2CJRJdpW7Uu82TfxLjpk"); // Replace "Qm..." with the actual hash

        // Retrieve the file from Node 2
        byte[] data = ipfsNode2.cat(fileHashFromNode1);
        System.out.println("Retrieved file content from Node 2: " + new String(data));
    }
}
