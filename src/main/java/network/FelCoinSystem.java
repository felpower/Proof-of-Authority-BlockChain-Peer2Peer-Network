package network;

import static peer.Role.MINER;
import static peer.Role.VALIDATOR;

import blockchain.Block;
import blockchain.Blockchain;
import helper.SignaturePublicKey;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import peer.Peer;
import transaction.Transaction;
import transaction.UpcomingTransaction;

public class FelCoinSystem {

  private final Set<Peer> peers = new HashSet<>();
  private Blockchain blockchain;
  private final Set<Transaction> transactionList = new HashSet<>();
  private final Map<UpcomingTransaction, List<SignaturePublicKey>> upcomingTransactions = new HashMap<>();

  public FelCoinSystem(Peer peer) {
    peers.add(peer);
    blockchain = new Blockchain();
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

  public Peer findPeerByWalletAdress(String receiver) {
    return peers.stream().filter(peer -> peer.hasAddress(receiver)).findFirst().orElse(null);
  }

  public long findActiveValidatorsInNetwork() {
    return getPeers().stream().filter(peer -> peer.hasRole(VALIDATOR)).count();
  }

  public Peer addRoleMinerToPeerWithPort(int port) {
    return peers.stream().filter(p -> p.hasPort(port)).findFirst().map(peer -> peer.addRole(MINER)).orElse(null);
  }

  public void addTransaction(Transaction transaction) {
    transactionList.add(transaction);
  }

  public Map<UpcomingTransaction, List<SignaturePublicKey>> getUpcomingTransactions() {
    return upcomingTransactions;
  }

  public int getAmountOfSignedUpcomingTransactions(UpcomingTransaction upcomingTransaction) {
    return upcomingTransactions.get(upcomingTransaction).size();
  }

  public void addUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactions.put(upcomingTransaction, new ArrayList<>());
  }

  public void removeUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactions.remove(upcomingTransaction);
  }

  public void removeTransaction(Transaction transaction) {
    transactionList.remove(transaction);
  }

  public void printTransactions() {
    if (transactionList.isEmpty()) {
      System.out.println("No Transactions in System");
    }
    for (Transaction transaction : transactionList) {
      System.out.println(transaction.toString());
    }
  }

  public boolean hasTransactions() {
    return !transactionList.isEmpty();
  }

  public void checkHeartbeat(Peer localPeer) {
    List<Peer> deadPeers = peers.stream()
        .map(peer -> peer.isAlive(LocalTime.now()))
        .filter(Objects::nonNull)
        .filter(peer -> !peer.equals(localPeer))
        .collect(Collectors.toList());

    if (deadPeers.size() >= 1) {
      Optional<Peer> miner = deadPeers.stream().filter(peer -> peer.hasRole(MINER)).findFirst();
      if (miner.isPresent()) {
        Sender.chooseMiner(localPeer, this);
      }
      deadPeers.forEach(peers::remove);
      System.out.println("Removed the following Peers, because no Heartbeat was sent in the last 10 seconds" + deadPeers);
    }
  }

  public Optional<Peer> findPeer(Peer peer) {
    return peers.stream().filter(peer1 -> peer1.hasAddress(peer.getAddress())).findFirst();
  }

  public Set<Transaction> getTransactions() {
    return transactionList;
  }
}
