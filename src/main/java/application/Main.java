package application;

import static java.lang.String.format;
import static java.lang.System.exit;
import static java.lang.System.in;
import static java.lang.System.out;
import static java.security.Security.addProvider;
import static peer.Role.MINER;
import static peer.Role.VALIDATOR;
import static peer.Sender.sendHello;
import static peer.Sender.synchronizeBlockchain;
import static transaction.UpcomingTransaction.newUpcomingTransaction;

import blockchain.Block;
import blockchain.Miner;
import certification.Certification;
import helper.SignaturePublicKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.util.Set;
import network.FelCoinSystem;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import peer.MultiReceiver;
import peer.Peer;
import peer.Role;
import peer.Sender;
import peer.SingleReceiver;
import transaction.UpcomingTransaction;

public class Main {
  //ToDo: Consensus of who is the Next miner, Merkle Root, Do not produce empty blocks(Only Allow when there are transactions in the System),
  //What happens when a Peer disconnects, without sending a disconnect(Probably use sockets)

  private static FelCoinSystem network;
  private static Peer peer;
  public static Wallet wallet;

  public static void main(String[] args) {
    addProvider(new BouncyCastleProvider());

    try {
      generateUserAndNetwork();
      startSenderReceiverThreads();
      start();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void generateUserAndNetwork() {
    //ToDo: Add Inputs for Port, for Username and for Role
    int port = (int) (Math.random() * (10)) + 5000;
    String username = "User " + (int) (Math.random() * (10));
    Role role = VALIDATOR;//Add Miner Role
    KeyPair keyPair = new Certification().generateKeyPair();
    peer = new Peer(port, username, keyPair, role);
    out.println("Created Peer: " + peer);
    wallet = new Wallet(keyPair);

    network = new FelCoinSystem(peer);
  }

  private static void startSenderReceiverThreads() throws InterruptedException {

    MultiReceiver multiReceiver = new MultiReceiver(peer, network);
    multiReceiver.start();

    SingleReceiver singleReceiver = new SingleReceiver(peer, network);
    singleReceiver.start();

    sendHello(peer);

    Thread.sleep(1000);//Wait for Peer Synchronization

    synchronizeBlockchain(peer, network);
    network.chooseMiner(peer);
  }

  public static void start() throws IOException {
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
//        case "2" -> {
//          createNewTransaction();
//          break;
//        }
        case "3" -> {
          showCurrentBalanceOfWallet();
          break;
        }
        case "4" -> {
          printCurrentBlockChain();
          break;
        }
        case "5" -> {
          mineNewBlock();
          break;
        }
//        case "6" -> {
//          showTotalValueOfCoinsInSystem();
//          break;
//        }
//        case "7" -> {
//          showActiveTransactions();
//          break;
//        }
        case "x" -> {
          out.println("Thanks for using FelCoin Peer Network");
          if (network.getPeers().size() > 1 && peer.hasRole(MINER)) {
            out.println("Miner is going offline");
            network.removePeer(peer);
            network.chooseMiner(peer);
          }
          Sender.disconnectPeer(peer);
          exit(1);
        }
      }
      out.print("input = ");
      input = reader.readLine();
    }
  }

  //  private static void showActiveTransactions() {
//    network.printTransactions();
//  }
//
//  private static void showTotalValueOfCoinsInSystem() {
//    out.println("The total amount of Coins in Rotation is " + network.getSystemBalance().values().stream().mapToInt(Double::intValue).sum());
//  }
//
  private static void printCurrentBlockChain() {
    network.getBlockchain().print();
  }


  private static void createNewTransaction() {
    try {
      UpcomingTransaction upcomingTransaction = newUpcomingTransaction(peer, wallet);

      SignaturePublicKey signaturePublicKey = new SignaturePublicKey(wallet.signTransaction(upcomingTransaction), wallet.getKeyPair().getPublic());
      upcomingTransaction.addSignaturePublicKey(signaturePublicKey);
      network.addUpcomingTransaction(upcomingTransaction);

//      network.proposeUpcomingTransactionToValidators(upcomingTransaction, peer);
//      Thread.sleep(1000);//Wait for Validators
//      upcomingTransaction = network.findUpcomingTransaction(upcomingTransaction);
////      if (upcomingTransaction.getValidated() >= network.findActiveValidatorsInNetwork()) {
//      Transaction transaction = new Transaction(upcomingTransaction, signaturePublicKey);
//      network.addTransaction(transaction);
//      network.removeUpcomingTransaction(upcomingTransaction);
//      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void mineNewBlock() {
    //ToDo: Only Allow when there are transactions in the System
    Block block = Miner.mineNewBlock(wallet, network.getBlockchain());
    Sender.sendBlock(block, peer);
  }

  private static void showCurrentBalanceOfWallet() {
    out.println(
        "The current Balance for the Wallet of the User "
            + peer.getUsername()
            + " is "
            + format("%.0f", wallet.getBalance().getAmount())
            + " "
            + wallet.getBalance().getName());
  }

  private static void showActivePeers() {
    Set<Peer> peers = network.getPeers();
    out.println("Currently active # of peer(s): " + peers.size() + ".... [x] marks your active peer");
    for (Peer peer : peers) {
      if (peer.equals(Main.peer)) {
        out.print("[x] ");
      }
      out.println(peer);
    }
  }

  private static void printInterface() {
    out.println("---------------------------------------");
    out.println("Enter 0 to show interface");
    out.println("Enter 1 to show Active Peers in Network");
    out.println("Enter 2 Create New Transaction");
    out.println("Enter 3 to show the Current Balance inside your Wallet");
    out.println("Enter 4 to show Current Block Chain");
    out.println("Enter 5 Mine New Block");
    out.println("Enter 6 to show Total Value Of Coins In the System");
    out.println("Enter 7 Show active Transactions in System");
    out.println("Enter x to disconnect from Peer Network");
    out.println("---------------------------------------");
  }
}