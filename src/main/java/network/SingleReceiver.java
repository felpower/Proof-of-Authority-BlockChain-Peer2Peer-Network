package network;

import static java.lang.System.out;

import application.Wallet;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import peer.Peer;

/*
Used this Documentation here for Receiver and Sender to create Multicast Sockets for Peer2Peer Connection
https://docs.oracle.com/javase/7/docs/api/java/net/MulticastSocket.html
 */
public class SingleReceiver extends Thread {

  private final Peer peer;
  private final FelCoinSystem network;
  private final Wallet wallet;
  private DatagramSocket socket;

  public SingleReceiver(Peer peer, FelCoinSystem network, Wallet wallet) {
    this.peer = peer;
    this.network = network;
    this.wallet = wallet;
  }

  @Override
  public void run() {
    try {
      socket = new DatagramSocket(peer.getPort());
      byte[] buf = new byte[20000];
      while (true) {
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        socket.receive(datagramPacket);
        String received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println("Message received: " + received);
        Packet packet = new Gson().fromJson(received, Packet.class);
        switch (packet.getAction()) {
          case "sync":
            Sender.sendCurrentBlockchain(network.getBlockchain(), network.getTransactionSet(), packet.getPeer());
            break;
          case "rsync":
            network.setBlockchain(packet.getBlockchain());
            network.setTransactionSet(packet.getTransactions());
            break;
          case "valUpTrans":
            network.addSignatureToUpcomingTransaction(packet.getUpcomingTransaction(), packet.getSignaturePublicKey());
            out.println("Upcoming Transaction Map " + network.getUpcomingTransactionMap().toString());
            break;
          case "valTrans":
            network.addTransaction(packet.getTransaction());
            break;
          case "sendCoins":
            wallet.addBalance(packet.getAmount());
            out.println("You just received: " + packet.getAmount() + " your new Balance is: " + wallet.getBalance().getAmount());
          default:
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      socket.close();
    }
  }
}
