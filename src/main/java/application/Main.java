package application;

import static java.lang.String.format;
import static java.lang.System.exit;
import static java.lang.System.in;
import static java.lang.System.out;
import static java.security.Security.addProvider;
import static network.Sender.sendHello;
import static network.Sender.synchronizeBlockchain;
import static peer.Role.MINER;
import static peer.Role.VALIDATOR;
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
import network.HeartbeatSender;
import network.MultiReceiver;
import network.Sender;
import network.SingleReceiver;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import peer.Peer;
import transaction.Transaction;
import transaction.UpcomingTransaction;

public class Main {

  public static Wallet wallet;
  private static FelCoinSystem network;
  private static Peer peer;

  public static void main(String[] args) {
    addProvider(new BouncyCastleProvider());

    try {
      generateUserAndNetwork();
      startSenderReceiverThreadsAndSynchronizeIntoNetwork();
      start();
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void generateUserAndNetwork() {
    //ToDo: Add Inputs for Port, for Username and for Role
    int port = (int) (Math.random() * (100)) + 5000;
    String username = "User " + (int) (Math.random() * (10));
    KeyPair keyPair = new Certification().generateKeyPair();
    peer = new Peer(port, username, keyPair, VALIDATOR);
    out.println("Created Peer: " + peer);
    wallet = new Wallet(keyPair);

    network = new FelCoinSystem(peer);
  }

  private static void startSenderReceiverThreadsAndSynchronizeIntoNetwork() throws InterruptedException {
    MultiReceiver multiReceiver = new MultiReceiver(peer, network, wallet);
    multiReceiver.start();

    SingleReceiver singleReceiver = new SingleReceiver(peer, network, wallet);
    singleReceiver.start();

    HeartbeatSender heartbeatSender = new HeartbeatSender(peer);
    heartbeatSender.start();

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
        case "0" -> printInterface();
        case "1" -> showActivePeers();
        case "2" -> createNewTransaction();
        case "3" -> showCurrentBalanceOfWallet();
        case "4" -> printCurrentBlockChain();
        case "5" -> mineNewBlock();
        case "6" -> showActiveTransactions();
        case "x" -> removePeer();
      }
      out.print("input = ");
      input = reader.readLine();
    }
  }

  private static void showActiveTransactions() {
    network.printTransactions();
  }

  private static void printCurrentBlockChain() {
    network.getBlockchain().print();
  }


  private static void createNewTransaction() {
    try {
      UpcomingTransaction upcomingTransaction = newUpcomingTransaction(peer, wallet);

      SignaturePublicKey signaturePublicKey = new SignaturePublicKey(wallet.signTransaction(upcomingTransaction), wallet.getKeyPair().getPublic());
      upcomingTransaction.addSignaturePublicKey(signaturePublicKey);
      network.addUpcomingTransaction(upcomingTransaction);

      Sender.proposeUpcomingTransactionToValidators(upcomingTransaction, peer);
      Thread.sleep(2000);//Wait for Validators to sign the Transaction
      if (network.checkForValidators(upcomingTransaction, peer)) {
        network.removeUpcomingTransaction(upcomingTransaction);
        Sender.sendCoins(upcomingTransaction, peer, network);
        Transaction transaction = new Transaction(upcomingTransaction, signaturePublicKey);
        network.addTransaction(transaction);
        Sender.sendFinalizedTransaction(peer, transaction);
        wallet.removeBalance(upcomingTransaction.getAmount());
        out.println("New Balance in Wallet is: " + wallet.getBalance().getAmount());
      } else {
        Sender.removeUpcomingTransaction(peer, upcomingTransaction);
        out.println("Transaction failed");
      }
      network.removeUpcomingTransaction(upcomingTransaction);
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static void mineNewBlock() {
    //Only Allow when there are transactions in the System, and you are the Miner
    if (!peer.hasRole(MINER)) {
      out.println("You can not mine new Blocks as you are not the miner");
      return;
    } else if (!network.hasTransactions()) {
      out.println("There are no active Transactions you can mine into a new Block");
      return;
    }
    Block block = Miner.mineNewBlock(wallet, network);
    Sender.sendBlock(block, peer);
    network.getTransactionSet().clear();
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

  private static void removePeer() {
    out.println("Thanks for using FelCoin Peer Network");
    if (network.getPeers().size() > 1 && peer.hasRole(MINER)) {
      out.println("Miner is going offline");
      network.removePeer(peer);
      network.chooseMiner(peer);
    }
    Sender.disconnectPeer(peer);
    exit(1);
  }

  private static void printInterface() {
    out.println("---------------------------------------");
    out.println("Enter 0 to show interface");
    out.println("Enter 1 to show Active Peers in Network");
    out.println("Enter 2 Create New Transaction");
    out.println("Enter 3 to show the Current Balance inside your Wallet");
    out.println("Enter 4 to show Current Block Chain");
    out.println("Enter 5 Mine New Block");
    out.println("Enter 6 Show active Transactions in System");
    out.println("Enter x to disconnect from Peer Network");
    out.println("---------------------------------------");
  }
}