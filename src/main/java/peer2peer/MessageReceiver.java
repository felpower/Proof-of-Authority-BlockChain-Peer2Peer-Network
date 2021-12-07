package peer2peer;

import static peer2peer.PeerInfo.getPeers;
import static peer2peer.PortSender.respondHandshake;

import application.Node;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Logger;

public class MessageReceiver extends Thread {

  Logger logger = Logger.getLogger(MessageReceiver.class.getName());

  protected MulticastSocket socket = null;
  protected Node node;

  public MessageReceiver(Node user) {
    this.node = user;
  }

  @Override
  public void run() {
    try {
      socket = new MulticastSocket(Config.port);

      InetAddress group = InetAddress.getByName(Config.ip_address);
      socket.joinGroup(group);

      while (true) {
        byte[] buffer = new byte[2056];
        DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

        socket.receive(incoming);
        String received = new String(incoming.getData(), 0, incoming.getLength());
        if ("end".equals(received)) {
          break;
        }
        Message<?> message = MessageParser.parseMessage(incoming);
        switch (message.getMessageType()) {
          case PeerDiscovery:
//            logger.info("New Peer found in Network " + message.getSender());
            if (!getPeers().contains(message.getSender())) {
              respondHandshake(message.getSender().getPort(), node);
              getPeers().add(message.getSender());
              //ToDo: add starting Balance
            }
            break;
          case NewBlock:

        }
      }

      socket.leaveGroup(group);
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
