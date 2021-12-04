package peer2peer;

import application.Node;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PeerInfo {

  private static Set<Node> peers = new HashSet<>();

  public static Set<Node> getPeers() {
    return peers;
  }

  public static void addPeer(Node peer) {
    peers.add(peer);
  }

  public static Node fetchRandPeer(Node requester) {
    if (peers.size() <= 1) {
      return requester;
    }
    int i = 0;
    for (Node node : peers) {
      if (i == new Random().nextInt(peers.size())) {
        if (node.equals(requester)) {
          return fetchRandPeer(requester);
        } else {
          return node;
        }
      }
      i++;
    }
    return null;
  }
}
