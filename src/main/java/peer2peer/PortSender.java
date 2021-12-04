package peer2peer;

import static java.net.InetAddress.getByName;
import static peer2peer.PeerInfo.fetchRandPeer;

import application.Node;
import blockchain.Block;
import blockchain.Block.Header;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PortSender {

  public static void respondHandshake(int port, Node node) throws IOException {
    Message<?> message = new Message<>(MessageType.PeerResponse, node, null);
    byte[] buffer = new ObjectMapper().writeValueAsBytes(message);

    new DatagramSocket().send(new DatagramPacket(buffer, buffer.length, getByName("localhost"), port));
  }

  public static void requestChainSync(Node requester, Block block) throws IOException
  {
    Message<Header> message    = new Message<>(MessageType.BlockchainSync, requester, block.getHeader());
    byte[] buffer = new ObjectMapper().writeValueAsBytes(message);

    int toPort = fetchRandPeer(requester).getPort();
    DatagramPacket packet  = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), toPort);
    new DatagramSocket().send(packet);
  }
}
