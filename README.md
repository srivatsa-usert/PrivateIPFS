# PrivateIPFS

In this repository there is File sending and retriever. Chat and call Application.
For both the Chat and call Application
The ceiver must be run before the sender

The below are some of the commands that were used when creating and using the nodes. And some were experimentations

##To setup nodes 

ipfs init                                              (done only once to setup a node) 

mkdir "$env:USERPROFILE\.ipfs_node1"                    (making the node directory)

$env:IPFS_PATH="$env:USERPROFILE\.ipfs_node1"          (to set the ipfs node to node1)

ipfs daemon                                             (to run the node)

notepad "$env:USERPROFILE\.ipfs_node1\config"              (to open the config file for the current node)

ipfs bootstrap rm --all                (remove all the bootstrap nodes)\

ipfs config Addresses.Gateway /ip4/127.0.0.1/tcp/8080               (To change the Addresses.Gateway )

ipfs config Addresses.API /ip4/127.0.0.1/tcp/5001                     (To change the Addresses.API)


set LIBP2P_FORCE_PNET=1              ()



ipfs daemon --enable-pubsub-experiment             (To start daemon with pubsub)


ipfs pubsub sub "name"        (To subscibe to the channel)

ipfs pubsub pub "name"        (To send message and keep it public) (Ctrl + C end the message)

#Sorry for not keeping it clean
