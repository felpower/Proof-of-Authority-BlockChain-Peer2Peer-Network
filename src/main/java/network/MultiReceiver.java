package network;

import static helper.IPHelper.getGroup;
import static helper.IPHelper.getPort;
import static network.Sender.acknowledgeHello;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Optional;
import peer.Peer;
import transaction.Transaction;

/*
Used this Docu here for Receiver and Sender to create Multicast Sockets for Peer2Peer Connection
https://docs.oracle.com/javase/7/docs/api/java/net/MulticastSocket.html
 */
public class MultiReceiver extends Thread {

  private final Peer peer;
  private final FelCoinSystem network;
  private MulticastSocket socket;
  private InetAddress group;

  public MultiReceiver(Peer peer, FelCoinSystem network) {
    this.peer = peer;
    this.network = network;
  }

  @Override
  public void run() {
    try {
      socket = new MulticastSocket(getPort());
      group = getGroup();
      socket.joinGroup(group);
      boolean ctd = true;
      while (ctd) {
        byte[] buf = new byte[20000];
        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
        socket.receive(datagramPacket);
        String received = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        Packet packet = new Gson().fromJson(received, Packet.class);
        switch (packet.getAction()) {
          case "heartbeat":
            Optional<Peer> peer = network.findPeer(packet.getPeer());
            peer.ifPresent(Peer::updateLastHeartbeat);
            network.checkHeartbeat(this.peer);
            break;
          case "New Peer":
            if (!packet.getPeer().equals(this.peer)) {
              network.addPeer(packet.getPeer());
              System.out.println("New Peer in System: " + packet.getPeer());
              acknowledgeHello(this.peer);
            }
            break;
          case "Ack":
            network.addPeer(packet.getPeer());
            break;
          case "block":
            if (!network.getBlockchain().getBlocks().contains(packet.getBlock())) {
              System.out.println("Received new Block: " + packet.getBlock());
              network.addBlock(packet.getBlock());
            }
            break;
          case "miner":
            Peer newMiner = network.addRoleMinerToPeerWithPort(packet.getPort());
            System.out.println("New Miner found: " + newMiner);
            break;
          case "DC":
            System.out.println("Peer went offline: " + packet.getPeer());
            network.removePeer(packet.getPeer());
            break;
          case "upTrans":
            Transaction transaction = new Transaction(packet.getUpcomingTransaction(), packet.getUpcomingTransaction().getSignaturePublicKey());
            network.addTransaction(transaction);
            Sender.validateTransaction(this.peer, transaction);
            break;
          case "remTrans":
            network.removeTransaction(packet.getTransaction());
            break;
          default:
            ctd = false;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        socket.leaveGroup(group);
      } catch (IOException e) {
        e.printStackTrace();
      }
      socket.close();
    }
  }

}