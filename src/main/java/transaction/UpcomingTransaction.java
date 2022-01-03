package transaction;

import static java.lang.Integer.parseInt;

import application.Peer;
import application.Wallet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

public class UpcomingTransaction implements Serializable {

  private String sender;
  private String receiver;
  private int amount;

  public UpcomingTransaction(String sender, String receiver, int amount) {
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
    return new UpcomingTransaction(peer.getAddress(), receivingAddress, amount);
  }

  public String getSender() {
    return sender;
  }

  public String getReceiver() {
    return receiver;
  }

  public int getAmount() {
    return amount;
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
