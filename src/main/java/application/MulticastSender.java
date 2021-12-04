package application;

import static peer2peer.Config.multicastHost;
import static peer2peer.Config.multicastPort;
import static peer2peer.MessageType.PeerDiscovery;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import peer2peer.Message;

public class MulticastSender {


  public static void requestHandshake(Node requester) {
    DatagramSocket socket = null;
    try {
      socket = new DatagramSocket();

      Message<?> message = new Message<>(PeerDiscovery, requester, null);
      byte[] buffer = new ObjectMapper().writeValueAsBytes(message);

      DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName(multicastHost), multicastPort);
      socket.send(packet);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      assert socket != null;
      socket.close();
    }
  }
}
