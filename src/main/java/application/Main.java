package application;

import static java.lang.String.format;
import static java.lang.System.exit;
import static java.lang.System.in;
import static java.lang.System.out;
import static java.security.Security.addProvider;
import static transaction.UpcomingTransaction.newUpcomingTransaction;

import blockchain.Block;
import blockchain.Miner;
import certification.Certification;
import helper.SignaturePublicKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import network.FelCoinSystem;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import transaction.Transaction;
import transaction.UpcomingTransaction;

/*
Inspiration for the network comes from the
 */
public class Main {

  public static Wallet wallet;
  private static FelCoinSystem network;
  private static Peer peer;

  public static void main(String[] args) {
    addProvider(new BouncyCastleProvider());

    try {
      int port = (int) (Math.random() * (6000 - 4999)) + 10000;
      String username = "User " + (int) (Math.random() * (10));
      KeyPair keyPair = new Certification().generateKeyPair();
      peer = new Peer(port, username, keyPair);
      network = new FelCoinSystem(peer);
      start();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void start() throws IOException {
    wallet = new Wallet(peer.getKeyPair());
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
        case "2" -> {
          createNewTransaction();
          break;
        }
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
        case "x" -> {
          out.println("Thanks for using FelCoin Peer Network");
          network.removePeer(peer);
          exit(1);
        }
      }
      out.print("input = ");
      input = reader.readLine();
    }
  }

  private static void printCurrentBlockChain() {
    network.getBlockchain().print();
  }

  private static void createNewTransaction() throws IOException {
    UpcomingTransaction upcomingTransaction = newUpcomingTransaction(peer, wallet);
    network.addUpcomingTransaction(upcomingTransaction);

    Transaction transaction = new Transaction(upcomingTransaction, new SignaturePublicKey(wallet.signTransaction(upcomingTransaction), wallet.getKeyPair()
        .getPublic()));
    network.removeTransaction(upcomingTransaction);

  }

  private static void mineNewBlock() {
    Block block = Miner.mineNewBlock(wallet, network.getBlockchain());
    network.getP2p().sendBlock(block);//ToDo: Only Allow when there are open transactions
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
    out.println("Currently active # of peer(s): " + network.getP2p().getPeers().size() + ".... [x] marks your active peer");
    for (Peer peer : network.getP2p().getPeers()) {
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
    out.println("Enter x to disconnect from Peer Network");
    out.println("---------------------------------------");
  }
}