package network;

import static helper.HashCode.applySha256;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
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
  private Set<Transaction> transactionSet = new HashSet<>();
  private final Map<UpcomingTransaction, Set<SignaturePublicKey>> upcomingTransactionMap = new HashMap<>();

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

  public void setBlockchain(Blockchain blockchain) {
    this.blockchain = blockchain;
  }

  public boolean addBlock(Block block) {
    return blockchain.addBlock(block);
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

  public long findActiveValidatorsExcludingSelfInNetwork(Peer self) {
    return getPeers().stream().filter(peer -> peer.hasRole(VALIDATOR)).filter(not(peer -> peer.equals(self))).count();
  }

  public Peer addRoleMinerToPeerWithPort(int port) {
    return peers.stream().filter(p -> p.hasPort(port)).findFirst().map(peer -> peer.addRole(MINER)).orElse(null);
  }

  public void addTransaction(Transaction transaction) {
    transactionSet.add(transaction);
  }

  public int getAmountOfSignedUpcomingTransactions(UpcomingTransaction upcomingTransaction) {
    return upcomingTransactionMap.get(upcomingTransaction).size();
  }

  public void addUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactionMap.putIfAbsent(upcomingTransaction, new HashSet<>());
  }

  public void removeUpcomingTransaction(UpcomingTransaction upcomingTransaction) {
    upcomingTransactionMap.remove(upcomingTransaction);
  }

  public void printTransactions() {
    if (transactionSet.isEmpty()) {
      System.out.println("No Transactions in System");
    }
    for (Transaction transaction : transactionSet) {
      System.out.println(transaction.toString());
    }
  }

  public boolean hasTransactions() {
    return !transactionSet.isEmpty();
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

  public Set<Transaction> getTransactionSet() {
    return transactionSet;
  }

  public void setTransactionSet(Set<Transaction> transactionSet) {
    this.transactionSet = transactionSet;
  }

  public List<String> getTransactionHashes() {
    return transactionSet.stream()
        .map(transaction -> applySha256(transaction.toString()))
        .collect(toList());
  }

  public void addSignatureToUpcomingTransaction(UpcomingTransaction upcomingTransaction, SignaturePublicKey signaturePublicKey) {
    upcomingTransactionMap.get(upcomingTransaction).add(signaturePublicKey);
  }

  public Map<UpcomingTransaction, Set<SignaturePublicKey>> getUpcomingTransactionMap() {
    return upcomingTransactionMap;
  }

  public boolean checkForValidators(UpcomingTransaction upcomingTransaction, Peer peer) {
    long validatorsInNetwork = findActiveValidatorsInNetwork();
    System.out.println("Validators in network total: " + validatorsInNetwork);
    validatorsInNetwork = findActiveValidatorsExcludingSelfInNetwork(peer);
    System.out.println("Validators in network excluding self: " + validatorsInNetwork);
    int amountOfSignedUpcomingTransactions = getAmountOfSignedUpcomingTransactions(upcomingTransaction);
    System.out.println("signed Transactions: " + amountOfSignedUpcomingTransactions);
    return amountOfSignedUpcomingTransactions >= validatorsInNetwork * 0.5;
  }
}
