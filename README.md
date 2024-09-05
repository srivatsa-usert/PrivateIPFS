
# IPFS File Transfer, Chat, and Call Application

This repository contains implementations for file transfer, a chat application, and a real-time audio call application using IPFS (InterPlanetary File System). Both the chat and call applications require the receiver to be run before the sender.

## Setup Instructions

### 1. IPFS Node Setup

To set up IPFS nodes, follow these steps:

1. Initialize the IPFS node (run once to set up the node):
   ```bash
   ipfs init
   ```

2. Create a directory for the node:
   ```bash
   mkdir "$env:USERPROFILE\.ipfs_node1"
   ```

3. Set the environment variable for the IPFS node path:
   ```bash
   $env:IPFS_PATH="$env:USERPROFILE\.ipfs_node1"
   ```

4. Start the IPFS daemon:
   ```bash
   ipfs daemon
   ```

5. Modify the IPFS node configuration file as needed:
   ```bash
   notepad "$env:USERPROFILE\.ipfs_node1\config"
   ```

6. Remove default bootstrap nodes (for private IPFS networks):
   ```bash
   ipfs bootstrap rm --all
   ```

7. Configure the API and Gateway addresses:
   ```bash
   ipfs config Addresses.Gateway /ip4/127.0.0.1/tcp/8080
   ipfs config Addresses.API /ip4/127.0.0.1/tcp/5001
   ```

8. Enable private networks (optional for secure communication):
   ```bash
   set LIBP2P_FORCE_PNET=1
   ```

### 2. PubSub Setup

For chat and call applications, IPFS PubSub is used for real-time message exchange. To enable PubSub:

1. Start the IPFS daemon with the PubSub experiment enabled:
   ```bash
   ipfs daemon --enable-pubsub-experiment
   ```

2. Subscribe to a channel (receiver side):
   ```bash
   ipfs pubsub sub "channel-name"
   ```

3. Publish messages to the channel (sender side):
   ```bash
   ipfs pubsub pub "channel-name" "your-message"
   ```

### 3. Running the Applications

- **File Sending and Retrieval:**
  - Use the provided Java programs to send and retrieve files through IPFS.
  
- **Chat Application:**
  - Run the receiver first to listen for messages.
  - The sender uploads messages to IPFS, retrieves the content identifier (CID), and sends it to the receiver via PubSub.

- **Call Application:**
  - Similar to the chat application, the receiver should start first to listen for real-time audio chunks.

### Notes

- To stop the IPFS daemon, press `Ctrl + C`.
- Ensure that both the sender and receiver nodes are properly configured and connected.
- This setup assumes both nodes are within the same network for private communication.

---
