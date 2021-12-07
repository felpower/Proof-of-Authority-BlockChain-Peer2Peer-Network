package application;

import static java.lang.System.in;
import static java.security.Security.addProvider;
import static peer2peer.MessageSender.sendMessage;
import static peer2peer.MessageType.DisconnectNode;
import static peer2peer.MessageType.PeerDiscovery;
import static peer2peer.PeerInfo.getPeers;
import static peer2peer.PortSender.synchronizeBlockChain;

import blockchain.BlockChain;
import blockchain.FelCoinNetwork;
import certification.Certification;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import peer2peer.MessageReceiver;
import peer2peer.PortReceiver;

/*
Inspiration for the whole network, especially peer2peer network comes from the hyperledger fabric implementation for the blockchain https://hyperledger-fabric.readthedocs.io/
 */
public class Main {

  private static BlockChain blockchain;

  public static void main(String[] args) {
    addProvider(new BouncyCastleProvider()); //Setup Bouncy castle as a Security Provider

    try {

      int port = (int) (Math.random() * (6000 - 4999));
      String username = "Test User" + (int) (Math.random() * (10));

      KeyPair keyPair = new Certification().generateKeyPair();
      Node node = new Node(port, username, keyPair);
      blockchain = new BlockChain();
      launch(node, keyPair);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static Wallet wallet;
  private static FelCoinNetwork felCoin;

  public static void launch(Node node, KeyPair keyPair) throws IOException {
    wallet = new Wallet(keyPair);
    felCoin = new FelCoinNetwork();

    MessageReceiver messageReceiver = new MessageReceiver(node);
    messageReceiver.setDaemon(true);
    messageReceiver.start();

    PortReceiver portListener = new PortReceiver(node);
    portListener.setDaemon(true);
    portListener.start();

    sendMessage(node, PeerDiscovery);
    synchronizeBlockChain(node, blockchain.getLastBlockInChain());

    sendMessage(node, DisconnectNode);
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String input = "0";
    while (true) {
      switch (input) {
        case "0" -> {
          printInterface();
          break;
        }
        case "1" -> {
          showActivePeers();
          break;
        }
        case "x" -> {
          System.out.println("Thanks for using FelCoin Peer Network");
          sendMessage(node, DisconnectNode);
          return;
        }
      }
      System.out.print("input = ");
      input = reader.readLine();
    }
  }

  private static void showActivePeers() {
    System.out.println("Currently active #" + getPeers().size() + " peer(s)");
    for (Node peer : getPeers()) {
      System.out.println(peer);
    }
  }

  private static void printInterface() {
    System.out.println("---------------------------------------");
    System.out.println("Enter 0 to show interface");
    System.out.println("Enter 1 to show Active Peers in Network");
    System.out.println("Enter 3 does nothing");
    System.out.println("Enter 4 does nothing");
    System.out.println("Enter 5 does nothing");
    System.out.println("Enter x to disconnect from Peer Network");
    System.out.println("---------------------------------------");
  }

  public static Wallet getWallet() {
    return wallet;
  }

  public static BlockChain getBlockchain() {
    return blockchain;
  }

  public static FelCoinNetwork getFelCoin() {
    return felCoin;
  }
}