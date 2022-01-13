package transaction;

import static java.lang.Integer.parseInt;

import application.Wallet;
import helper.SignaturePublicKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import peer.Peer;

public class UpcomingTransaction implements Serializable {

  private Peer sender;
  private String receiver;
  private double amount;
  private SignaturePublicKey signaturePublicKey;

  public UpcomingTransaction() {
  }

  public UpcomingTransaction(Peer sender, String receiver, double amount) {
    this.sender = sender;
    this.receiver = receiver;
    this.amount = amount;
  }

  public static UpcomingTransaction newUpcomingTransaction(Peer peer, Wallet wallet) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    System.out.println("Enter the address of Peer where you want to send your Coins to: ");
    String receivingAddress = reader.readLine();
    System.out.println("Enter the amout of Felcoins you want to send: ");

    int amount = -1;
    while (amount > wallet.getBalance().getAmount() || amount < 0) {
      amount = parseInt(reader.readLine());
    }
    return new UpcomingTransaction(peer, receivingAddress, amount);
  }


  public void addSignaturePublicKey(SignaturePublicKey signaturePublicKey) {
    this.signaturePublicKey = signaturePublicKey;
  }

  public Peer getSender() {
    return sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public double getAmount() {
    return amount;
  }


  public SignaturePublicKey getSignaturePublicKey() {
    return signaturePublicKey;
  }

  @Override
  public String toString() {
    return "UpcomingTransaction{" +
        "sender='" + sender + '\'' +
        ", receiver='" + receiver + '\'' +
        ", amount=" + amount +
        '}';
  }
}
