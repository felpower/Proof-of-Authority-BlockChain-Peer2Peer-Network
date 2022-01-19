package network;

import static peer.Role.MINER;
import static peer.Role.VALIDATOR;

import blockchain.Block;
import blockchain.Blockchain;
import helper.SignaturePublicKey;
import java.time.LocalTime;
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
  private Set<Transaction> transactions = new HashSet<>();
  private Map<UpcomingTransaction, Set<SignaturePublicKey>> upcomingTransactions = new HashMap<>();

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

  public boolean addBlock(Block block) {
   return blockchain.addBlock(block);
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
    transactions.add(transaction);
  }

  public int getAmountOfSignedUpcomingTransactions(UpcomingTransaction upcomingTransaction) {
    return upcomingTransactions.get(upcomingTransaction).size();
  }

  public void addUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactions.put(upcomingTransaction, new HashSet<>());
  }

  public void removeUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactions.remove(upcomingTransaction);
  }

  public void printTransactions() {
    if (transactions.isEmpty()) {
      System.out.println("No Transactions in System");
    }
    for (Transaction transaction : transactions) {
      System.out.println(transaction.toString());
    }
  }

  public boolean hasTransactions() {
    return !transactions.isEmpty();
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
    return transactions;
  }

  public void addSignatureToUpcomingTransaction(UpcomingTransaction upcomingTransaction, SignaturePublicKey signaturePublicKey) {
    upcomingTransactions.get(upcomingTransaction).add(signaturePublicKey);
  }

  public Map<UpcomingTransaction, Set<SignaturePublicKey>> getUpcomingTransactions() {
    return upcomingTransactions;
  }

  public void setTransactions(Set<Transaction> transactions) {
    this.transactions = transactions;
  }

  public void setUpcomingTransactions(Map<UpcomingTransaction, Set<SignaturePublicKey>> upcomingTransactions) {
    this.upcomingTransactions = upcomingTransactions;
  }
}
