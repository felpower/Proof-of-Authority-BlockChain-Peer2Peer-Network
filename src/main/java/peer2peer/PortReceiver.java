package peer2peer;

import static application.Main.getFelCoin;
import static peer2peer.MessageParser.parseMessage;

import application.Node;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

public class PortReceiver extends Thread {

  Logger logger = Logger.getLogger(PortReceiver.class.getName());

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

        Message<?> message = parseMessage(incoming);

        switch (message.getMessageType()) {
          case PeerResponse:
//            logger.info("new peer response from " + message.getSender());
            PeerInfo.addPeer(message.getSender());

            // adds address to chain accounts
            getFelCoin().getCoins().put(message.getSender().getAddress(), 1000F);
            break;

        }
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
