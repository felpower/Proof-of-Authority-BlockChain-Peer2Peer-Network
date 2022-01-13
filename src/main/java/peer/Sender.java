package peer;

import static helper.IPHelper.getPort;
import static helper.RandomHelper.getRandomPeer;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;

import blockchain.Block;
import blockchain.Blockchain;
import com.google.gson.Gson;
import helper.IPHelper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import network.FelCoinSystem;
import network.Packet;
import transaction.Transaction;
import transaction.UpcomingTransaction;

/*
Used this Docu here for Receiver and Sender to create Multicast Sockets for Peer2Peer Connection
https://docs.oracle.com/javase/7/docs/api/java/net/MulticastSocket.html
 */
public class Sender {

  public static void sendHello(Peer peer) {
    sendMultiPacket(new Packet("New Peer", peer));
  }

  public static void acknowledgeHello(Peer peer) {
    sendMultiPacket(new Packet("Ack", peer));
  }

  public static void sendBlock(Block block, Peer peer) {
    sendMultiPacket(new Packet("block", peer, block));
  }

  public static void synchronizeBlockchain(Peer peer, FelCoinSystem network) {
    Peer randomPeer = getRandomPeer(network.getPeers(), singletonList(peer));
    if (nonNull(randomPeer)) {
      sendSinglePacket(new Packet("sync", peer), randomPeer.getPort());
    }
  }

  public static void sendCurrentBlockchain(Blockchain blockchain, Peer peer) {
    sendSinglePacket(new Packet("resync", peer, blockchain), peer.getPort());
  }

  public static void chooseMiner(Peer peer, FelCoinSystem network) {
    Peer randomPeer = getRandomPeer(network.getPeers(), emptyList());
    if (nonNull(randomPeer)) {
      System.out.println("New Miner is going to be: " + randomPeer);
      sendMultiPacket(new Packet("miner", peer, randomPeer.getPort()));
    }
  }

  public static void proposeUpcomingTransactionToValidators(UpcomingTransaction upcomingTransaction, Peer peer) {
    sendMultiPacket(new Packet("upTrans", peer, upcomingTransaction));
  }

  public static void validateTransaction(Peer peer, Transaction transaction) {
    sendSinglePacket(new Packet("valTrans", peer, transaction), transaction.getPortFromCreater());
  }

  public static void disconnectPeer(Peer peer) {
    sendMultiPacket(new Packet("DC", peer));
  }

  private static void sendSinglePacket(Packet packet, int port) {
    try {
      sendPacket(packet, port, IPHelper.getLocalGroup());
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  private static void sendMultiPacket(Packet packet) {
    try {
      sendPacket(packet, getPort(), IPHelper.getGroup());
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }

  private static void sendPacket(Packet packet, int port, InetAddress group) {
    try {
      byte[] buffer = new Gson().toJson(packet).getBytes();
      DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, group, port);
      new DatagramSocket().send(datagramPacket);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void sendCoins(UpcomingTransaction upcomingTransaction, Peer peer, FelCoinSystem network) {
    Peer receiver = network.findPeerByWalletAdress(upcomingTransaction.getReceiver());
    sendSinglePacket(new Packet("sendCoins", peer, upcomingTransaction.getAmount()), receiver.getPort());
  }
}
