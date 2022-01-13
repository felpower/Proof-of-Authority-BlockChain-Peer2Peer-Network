package network;

import static peer.Role.MINER;

import blockchain.Block;
import blockchain.Blockchain;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import peer.Peer;
import peer.Sender;
import transaction.Transaction;
import transaction.UpcomingTransaction;

public class FelCoinSystem {

  private final Set<Peer> peers = new HashSet<>();
  private Blockchain blockchain;
  public List<Transaction> transactionList;
  public List<UpcomingTransaction> upcomingTransactionList;

  public FelCoinSystem(Peer peer) {
    peers.add(peer);
    this.blockchain = new Blockchain();
  }

  public Set<Peer> getPeers() {
    return peers;
  }

  public void addPeer(Peer peer) {
    peers.add(peer);
  }

  public void removePeer(Peer peer) {
    peers.remove(peer);
  }

  public Blockchain getBlockchain() {
    return blockchain;
  }

  public void addBlock(Block block) {
    blockchain.addBlock(block);
  }

  public void setBlockchain(Blockchain blockchain) {
    this.blockchain = blockchain;
  }

  public void chooseMiner(Peer peer) {
    if (peers.stream().noneMatch(p -> p.hasRole(MINER))) {
      Sender.chooseMiner(peer, this);
    }
  }

  public Peer addRoleMinerToPeerWithPort(int port) {
    return peers.stream().filter(p->p.hasPort(port)).findFirst().map(peer -> peer.addRole(MINER)).orElse(null);
  }

  public void addUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactionList.add(upcomingTransaction);
  }
}
