package network;

import static helper.IPHelper.getPort;
import static helper.RandomHelper.getRandomPeer;
import static java.util.Collections.singletonList;
import static java.util.Objects.nonNull;

import blockchain.Block;
import blockchain.Blockchain;
import com.google.gson.Gson;
import helper.IPHelper;
import helper.SignaturePublicKey;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import peer.Peer;
import transaction.Transaction;
import transaction.UpcomingTransaction;

/*
Used this Documentation here for Receiver and Sender to create Multicast Sockets for Peer2Peer Connection
https://docs.oracle.com/javase/7/docs/api/java/net/MulticastSocket.html
 */
public class Sender {

  public static void sendHello(Peer peer) {
    sendMultiPacket(new Packet("New Peer", peer));
  }

  public static void sendHeartbeat(Peer peer) {
    sendMultiPacket(new Packet("heartbeat", peer));
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

  public static void sendCurrentBlockchain(
      Blockchain blockchain,
      Set<Transaction> transactions,
      Peer peer) {
    sendSinglePacket(new Packet("rsync", peer, blockchain, transactions), peer.getPort());
  }

  public static void chooseMiner(Peer peer, Peer oldMiner, FelCoinSystem network) {
    List<Peer> oldMinerList = new ArrayList<>();
    if (nonNull(oldMiner)) {
      oldMinerList.add(oldMiner);
    }
    Peer randomPeer = getRandomPeer(network.getPeers(), oldMinerList);
    if (nonNull(randomPeer)) {
      sendMultiPacket(new Packet("miner", peer, randomPeer.getPort()));
    }
  }

  public static void chooseMiner(Peer newMiner) {
    sendMultiPacket(new Packet("miner", newMiner, newMiner.getPort()));
  }

  public static void proposeUpcomingTransactionToValidators(UpcomingTransaction upcomingTransaction, Peer peer) {
    sendMultiPacket(new Packet("upTrans", peer, upcomingTransaction));
  }

  public static void validatedUpcomingTransaction(Peer peer, Peer sender, UpcomingTransaction upcomingTransaction,
      SignaturePublicKey signaturePublicKey) {
    System.out.println("Sending Package to peer: " + sender.toString());
    sendSinglePacket(new Packet("valUpTrans", peer, upcomingTransaction, signaturePublicKey), sender.getPort());
  }

  public static void sendFinalizedTransaction(Peer peer, Transaction transaction) {
    sendMultiPacket(new Packet("finalTrans", peer, transaction));
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

  public static void removeUpcomingTransaction(Peer peer, UpcomingTransaction upcomingTransaction) {
    sendMultiPacket(new Packet("remTrans", peer, upcomingTransaction));
  }
}
