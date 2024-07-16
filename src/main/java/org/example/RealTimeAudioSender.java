//Part of the call Application. Need to run RealTimeAudioReceiver before running this file so that audio can be sent and received.
//Currently working withing private IPFS connected to same local network(using local IP).
//Need to add IPFS ping before sending to check if the receiver is ready to accept.
//Points to develop - need to IPFS ping to check the availablilty of the receiver
//                  - Need to be able to setup and use the private IPFS network and send to a node in public network
//                    (Need to explore how the private IPFS node connects to another node in network.)
//                    (After running daemon, by running IPFS id need to know what the node announces about the IP and peer ID and how it is selecting)

package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;

public class RealTimeAudioSender {
    private static final int SAMPLE_RATE = 44100; // CD quality
    private static final int SAMPLE_SIZE_IN_BITS = 16; // Standard for high quality
    private static final int CHANNELS = 2; // Stereo
    private static final int FRAMES_PER_CHUNK = SAMPLE_RATE * 2; // 2 seconds of audio

    private final IPFS ipfs;
    private final Socket socket;
    private final PrintWriter out;

    public RealTimeAudioSender(String multiAddr, String receiverHost, int receiverPort) throws IOException {
        this.ipfs = new IPFS(multiAddr);
        this.socket = new Socket(receiverHost, receiverPort);
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public void start() throws LineUnavailableException, IOException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, true, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine line = (TargetDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();

        byte[] buffer = new byte[FRAMES_PER_CHUNK * format.getFrameSize()];
        int bytesRead;

        while (true) {
            bytesRead = line.read(buffer, 0, buffer.length);
            if (bytesRead > 0) {
                File chunkFile = new File("chunk.wav");
                try (ByteArrayInputStream bais = new ByteArrayInputStream(buffer, 0, bytesRead);
                     AudioInputStream chunkStream = new AudioInputStream(bais, format, bytesRead / format.getFrameSize())) {
                    AudioSystem.write(chunkStream, AudioFileFormat.Type.WAVE, chunkFile);
                }

                String hash = uploadFile(chunkFile);
                sendHash(hash);
            }
        }
    }

    private String uploadFile(File file) throws IOException {
        NamedStreamable.FileWrapper fileWrapper = new NamedStreamable.FileWrapper(file);
        MerkleNode addResult = ipfs.add(fileWrapper).get(0);
        System.out.println(addResult.hash.toString());
        return addResult.hash.toString();
    }

    private void sendHash(String hash) {
        out.println(hash);
    }

    public static void main(String[] args) throws IOException, LineUnavailableException {
        String receiverHost = "127.0.0.1"; // Replace with the receiver's IP address
        int receiverPort = 5000; // Ensure this port is open and not blocked by firewall

        RealTimeAudioSender sender = new RealTimeAudioSender("/ip4/127.0.0.1/tcp/5001", receiverHost, receiverPort);
        sender.start();
    }
}
