package helper;

import application.Peer;
import java.util.List;
import java.util.Random;

public class RandomHelper {

  public static Peer getRandomPeer(List<Peer> peerList, List<Peer> except) {
    peerList.removeAll(except);
    return peerList.get(new Random().nextInt(peerList.size()));
  }

}
