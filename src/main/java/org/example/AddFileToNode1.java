//To test sending of a file from a Node to different node

package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;

import java.io.IOException;

public class AddFileToNode1 {

    public static void main(String[] args) throws IOException {
        // Initialize IPFS client for Node 1
        IPFS ipfsNode1 = new IPFS("/ip4/127.0.0.1/tcp/5001");

        // Add a file to Node 1
        NamedStreamable.ByteArrayWrapper file = new NamedStreamable.ByteArrayWrapper("hello.txt", "Hello IPFS from Node 1!".getBytes());
        MerkleNode addResult = ipfsNode1.add(file).get(0);
        Multihash fileHash = addResult.hash;
        System.out.println("Added file hash from Node 1: " + fileHash);
    }
}
