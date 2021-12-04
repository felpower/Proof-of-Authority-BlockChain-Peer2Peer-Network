package blockchain;

import static java.lang.System.in;

import application.Application;
import application.Node;
import certification.Certification;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.security.Security;
import java.util.HashMap;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import transaction.TransactionOutput;

/*
Inspiration for the whole network, especially peer2peer network comes from the hyperledger fabric implementation for the blockchain https://hyperledger-fabric.readthedocs.io/
 */
public class Main {

  private static BlockChain blockchain;

  public static HashMap<String, TransactionOutput> UTXOs = new HashMap<>();

  public static float minimumTransaction = 0.1f;

  public static void main(String[] args) {
    Security.addProvider(new BouncyCastleProvider()); //Setup Bouncey castle as a Security Provider

    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    try {
      System.out.print("Enter your port number > ");
      int port = Integer.parseInt(reader.readLine());

      System.out.print("enter your username > ");
      String username = reader.readLine();

      KeyPair keyPair = new Certification().generateKeyPair();
      Node node = new Node(port, username, keyPair);
      blockchain = new BlockChain();
      Application.launch(node, keyPair, blockchain);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}