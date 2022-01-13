package peer;

import application.Wallet;
import blockchain.Blockchain;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import network.FelCoinSystem;
import network.Packet;

/*
Used this Docu here for Receiver and Sender to create Multicast Sockets for Peer2Peer Connection
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
      boolean ctd = true;
      while (ctd) {
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        socket.receive(datagramPacket);
        String received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println("Message received: " + received);
        Packet packet = new Gson().fromJson(received, Packet.class);
        switch (packet.getAction()) {
          case "sync":
            Blockchain blockchain = network.getBlockchain();
            Sender.sendCurrentBlockchain(blockchain, packet.getPeer());
            break;
          case "resync":
            network.setBlockchain(packet.getBlockchain());
            break;
          case "valTrans":
            network.addTransaction(packet.getTransaction());
            break;
          case "sendCoins":
            wallet.addBalance(packet.getAmount());
          default:
            ctd = false;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      socket.close();
    }
  }

}
