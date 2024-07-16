//Part of the call Application. Need to run RealTimeAudioReceiver before running this file so that audio can be sent and received.
//Currently working withing private IPFS connected to same local network(using local IP).
//Need to add IPFS ping before sending to check if the receiver is ready to accept.
//Points to develop - need to IPFS ping to check the availablilty of the receiver
//                  - Need to be able to setup and use the private IPFS network and send to a node in public network
//                    (Need to explore how the private IPFS node connects to another node in network.)
//                    (After running daemon, by running IPFS id need to know what the node announces about the IP and peer ID and how it is selecting)

package org.example;

import io.ipfs.api.IPFS;
import io.ipfs.multihash.Multihash;

import javax.sound.sampled.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class RealTimeAudioReceiver {
    private static final int SAMPLE_RATE = 44100; // CD quality
    private static final int SAMPLE_SIZE_IN_BITS = 16; // Standard for high quality
    private static final int CHANNELS = 2; // Stereo

    private final IPFS ipfs;
    private final ServerSocket serverSocket;

    public RealTimeAudioReceiver(String multiAddr, int port) throws IOException {
        this.ipfs = new IPFS(multiAddr);
        this.serverSocket = new ServerSocket(port);
    }

    public void start() throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        Socket clientSocket = serverSocket.accept();
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        SourceDataLine speaker = getSpeaker();

        String hash;
        while ((hash = in.readLine()) != null) {
            File chunkFile = downloadFile(hash);
            playChunk(chunkFile, speaker);
        }
    }

    private File downloadFile(String hash) throws IOException {
        Multihash filePointer = Multihash.fromBase58(hash);
        System.out.println(filePointer);
        byte[] fileContents = ipfs.cat(filePointer);
        File file = File.createTempFile("chunk", ".wav");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileContents);
        }
        return file;
    }

    private SourceDataLine getSpeaker() throws LineUnavailableException {
        AudioFormat format = new AudioFormat(SAMPLE_RATE, SAMPLE_SIZE_IN_BITS, CHANNELS, true, true);
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(format);
        line.start();
        return line;
    }

    private void playChunk(File chunkFile, SourceDataLine speaker) throws IOException, UnsupportedAudioFileException {
        try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(chunkFile)) {
            byte[] buffer = new byte[speaker.getBufferSize() / 5];
            int bytesRead;
            while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                speaker.write(buffer, 0, bytesRead);
            }
        }
    }

    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        RealTimeAudioReceiver receiver = new RealTimeAudioReceiver("/ip4/127.0.0.1/tcp/5001", 5000);
        receiver.start();
    }
}