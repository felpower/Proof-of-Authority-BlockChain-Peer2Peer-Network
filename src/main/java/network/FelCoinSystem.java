package network;


import static application.Role.VALIDATOR;

import application.Peer;
import blockchain.Block;
import blockchain.Blockchain;
import java.util.List;
import java.util.Map;
import transaction.Transaction;
import transaction.UpcomingTransaction;

public class FelCoinSystem {

  private final Peer peer;
  private final Peer2PeerInterface p2p;
  public Blockchain blockchain;
  public Map<String, Double> systemBalance;
  public List<Transaction> transactionList;

  public List<UpcomingTransaction> upcomingTransactionList;

  public FelCoinSystem(final Peer peer) {
    this.peer = peer;
    this.p2p = new Peer2PeerNetwork(this, peer);
    this.blockchain = p2p.getCurrentChain(peer);
    this.systemBalance = p2p.getSystemBalance(peer);
    this.transactionList = p2p.getTransactionList(peer);
    this.upcomingTransactionList = p2p.getUpcomingTransactionList(peer);
  }

  public Peer2PeerInterface getP2p() {
    return p2p;
  }

  public List<Peer> getPeers() {
    return p2p.getPeers();
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

  public void proposeUpcomingTransactionToValidators(UpcomingTransaction upcomingTransaction, Peer peer) {
    p2p.proposeTransactionToValidators(upcomingTransaction, peer);
  }

//  public boolean signTransaction(UpcomingTransaction upcomingTransaction) {
//    if (SignaturePublicKey.signTransaction(upcomingTransaction, peer.getKeyPair())) {
//      transactionList.add(new Transaction(upcomingTransaction, upcomingTransaction.getSignaturePublicKey()));
//      return true;
//    }
//    System.out.println("Error signing Transaction");
//    return false;
//  }

  public void removeUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactionList.remove(upcomingTransaction);
  }

  public long findActiveValidatorsInNetwork() {
    return getPeers().stream().filter(peer -> peer.getRole().equals(VALIDATOR)).count();
  }

  public void addTransaction(Transaction transaction) {
    transactionList.add(transaction);
  }

  public void removePeer(Peer peer) {
    p2p.removePeer(peer);
  }

  public Blockchain getBlockchain() {
    return blockchain;
  }

  public Map<String, Double> getSystemBalance() {
    return systemBalance;
  }

  public UpcomingTransaction findUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    return upcomingTransactionList.stream()
        .filter(transaction -> transaction.equals(upcomingTransaction))
        .findFirst().orElseThrow();
  }

}
