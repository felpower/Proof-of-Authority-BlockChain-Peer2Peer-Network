package network;

import application.Peer;
import blockchain.Block;
import blockchain.Blockchain;
import java.util.List;
import java.util.Map;
import transaction.Transaction;
import transaction.UpcomingTransaction;

public interface Peer2PeerInterface {

  public Blockchain getCurrentChain(Peer peer);

  public List<Peer> getPeers();

  public void sendBlock(Block block);

  public boolean removePeer(Peer peer);

//  public boolean signTransaction(UpcomingTransaction upcomingTransaction);

  Map<String, Double> getSystemBalance(Peer peer);

  void proposeTransactionToValidators(UpcomingTransaction upcomingTransaction, Peer peer);

  List<Transaction> getTransactionList(Peer peer);

  List<UpcomingTransaction> getUpcomingTransactionList(Peer peer);
}
