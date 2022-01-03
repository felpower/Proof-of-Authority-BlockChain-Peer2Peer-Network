package network;


import application.Peer;
import blockchain.Block;
import blockchain.Blockchain;
import java.util.ArrayList;
import java.util.List;
import transaction.UpcomingTransaction;

public class FelCoinSystem {

  private final Peer2PeerInterface p2p;
  public Blockchain blockchain;

  public List<UpcomingTransaction> upcomingTransactionList = new ArrayList<>();

  public FelCoinSystem(final Peer peer) {
    p2p = new Peer2PeerNetwork(this, peer);
    blockchain = p2p.getCurrentChain();
  }

  public Peer2PeerInterface getP2p() {
    return p2p;
  }

  public boolean addBlock(Block block) {
    if (blockchain.addBlock(block)) {
      System.out.println("Added block: " + block.toString());
      return true;
    }

    return false;
  }

  public void addUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactionList.add(upcomingTransaction);
  }

  public Blockchain getBlockchain() {
    return blockchain;
  }

  public void removePeer(Peer peer) {
    p2p.removePeer(peer);
  }

  public void removeTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactionList.remove(upcomingTransaction);
  }
}
