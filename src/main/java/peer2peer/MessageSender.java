package peer2peer;

import static java.net.InetAddress.getByName;
import static peer2peer.Config.ip_address;
import static peer2peer.Config.port;

import application.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MessageSender {

  public static void sendMessage(Node node, MessageType disconnectNode) {
    DatagramSocket socket = null;
    try {
      socket = new DatagramSocket();
      byte[] buffer = new ObjectMapper().writeValueAsBytes(new Message<>(disconnectNode, node, null));

      DatagramPacket packet = new DatagramPacket(buffer, buffer.length, getByName(ip_address), port);
      socket.send(packet);

    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      assert socket != null;
      socket.close();
    }
  }
}
