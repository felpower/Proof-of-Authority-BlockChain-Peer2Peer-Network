package blockchain;

import helper.HashCode;
import helper.SignaturePublicKey;
import java.util.List;
import transaction.Transaction;

public class Block {

  //The hash of this block, calculated based on other data
  public String hash;
  // The signature and its belonging public key
  private final SignaturePublicKey signaturePublicKey;
  //The list of Transactions
  public List<Transaction> transactions;

  // Data to make the Block unique
  public Header header;

  public Block(
      int nonce,
      String merkleRoot,
      String previousHash,
      long timestamp,
      List<Transaction> transactions,
      SignaturePublicKey signaturePublicKey) {
    this.header = new Header(previousHash, timestamp, nonce, merkleRoot);
    this.signaturePublicKey = signaturePublicKey;
    this.transactions = transactions;
    this.hash = calculateHash(nonce, merkleRoot, previousHash, timestamp);
  }

  protected static String calculateHash(int nonce, String merkleRoot, String previousHash, long timestamp) {
    return HashCode.applySha256(nonce + merkleRoot + previousHash + timestamp);
  }

  public String getHash() {
    return hash;
  }

  public String getPreviousHash() {
    return header.previousHash;
  }

  public long getTimestamp() {
    return header.timestamp;
  }

  public int getNonce() {
    return header.nonce;
  }

  public String getMerkleRoot() {
    return header.merkleRoot;
  }

  public SignaturePublicKey getSignaturePublicKey() {
    return signaturePublicKey;
  }

  public List<Transaction> getTransactions() {
    return transactions;
  }

  public Header getHeader() {
    return header;
  }

  public static class Header {

    //Hash of the previous block, an important part to build the chain
    public String previousHash;
    //The timestamp of the creation of this block
    public long timestamp;
    //A nonce, which is an arbitrary number used in cryptography
    public int nonce;
    // to make sure data blocks passed between peers on a peer-to-peer network are whole, undamaged, and unaltered.
    public String merkleRoot;

    public Header(String previousHash, long timestamp, int nonce, String merkleRoot) {
      this.previousHash = previousHash;
      this.timestamp = timestamp;
      this.nonce = nonce;
      this.merkleRoot = merkleRoot;
    }
  }
  //  //Block Constructor.
//  public Block(String previousHash) {
//    this.previousHash = previousHash;
//    this.timeStamp = new Date().getTime();
//
//    this.hash = calculateHash(); //Making sure we do this after we set the other values.
//  }
//
//  //Calculate new hash based on blocks contents
//  public String calculateHash() {
//    return applySha256(
//        previousHash +
//            timeStamp +
//            nonce +
//            merkleRoot
//    );
//  }
//
//  //Increases nonce value until hash target is reached.
//  public void mineBlock(int difficulty) {
//    merkleRoot = getMerkleRoot(transactions);
//    String target = getDificultyString(difficulty);
//    while (!hash.substring(0, difficulty).equals(target)) {
//      nonce++;
//      hash = calculateHash();
//    }
//    System.out.println("Block Mined!!! : " + hash);
//  }
//
//  //Add transactions to this block
//  public void addTransaction(Transaction transaction) {
//    //process transaction and check if valid, unless block is genesis block then ignore.
//    if (transaction == null) {
//      return;
//    }
//    if (!Objects.equals(previousHash, "0")) {
//      if ((!transaction.processTransaction())) {
//        System.out.println("Transaction failed to process. Discarded.");
//        return;
//      }
//    }
//    transactions.add(transaction);
//    System.out.println("Transaction Successfully added to Block");
//  }

}