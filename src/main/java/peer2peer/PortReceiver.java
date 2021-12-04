package peer2peer;

import application.Application;
import application.Node;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PortReceiver extends Thread {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  protected MulticastSocket socket = null;
  protected Node node;

  public PortReceiver(Node user) {
    this.node = user;
  }

  @Override
  public void run() {
    try {
      DatagramSocket serverSocket = new DatagramSocket(node.getPort());
      byte[] buffer = new byte[16384];
      while (true) {
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
        serverSocket.receive(incoming);

        Message<?> message = MessageParser.parseMessage(incoming);

        switch (message.getMessageType()) {
          case PeerResponse:
            logger.info("new peer response from " + message.getSender());
            PeerInfo.addPeer(message.getSender());

            // adds address to chain accounts
            Application.getFelCoin().getCoins().put(message.getSender().getAddress(), 1000F);
            break;

        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
