package helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import peer.Peer;

public class RandomHelper {

  public static Peer getRandomPeer(Set<Peer> peers, List<Peer> except) {
    ArrayList<Peer> peerList = new ArrayList<>(peers);
    peerList.removeAll(except);
    if (peerList.size() <= 0) {
      System.out.println("No peers found");
      return null;
    }
    return peerList.get(new Random().nextInt(peerList.size()));
  }

}
