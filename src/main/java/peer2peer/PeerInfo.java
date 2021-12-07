package peer2peer;

import application.Node;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class PeerInfo {

  private static final Set<Node> peers = new HashSet<>();

  public static Set<Node> getPeers() {
    return peers;
  }

  public static void addPeer(Node peer) {
    peers.add(peer);
  }

  public static Node getRandomPeer(Node requester) {
    Random rand = new Random();
    if (peers.size() > 1) {
      int index = rand.nextInt(peers.size());
      Iterator<Node> iter = peers.iterator();
      for (int i = 0; i < index; i++) {
        iter.next();
      }
      return iter.next();
    }
    return requester;
  }
}
