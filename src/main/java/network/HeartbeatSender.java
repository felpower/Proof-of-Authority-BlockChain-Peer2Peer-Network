package network;

import peer.Peer;

public class HeartbeatSender extends Thread {

  private final Peer peer;

  public HeartbeatSender(Peer peer) {
    this.peer = peer;
  }

  @Override
  public void run() {
    try {
      while (true) {
        Thread.sleep(2000);
        Sender.sendHeartbeat(peer);
      }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
