package blockchain;

import static helper.HashCode.applySha256;

import helper.SignaturePublicKey;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import transaction.Transaction;

public class Block implements Serializable {

  // The signature and its belonging public key
  private final SignaturePublicKey signaturePublicKey;
  //The hash of this block, calculated based on other data
  public String hash;
  //The list of Transactions
  public Set<Transaction> transactions = new HashSet<>();
  //Hash of the previous block, an important part to build the chain
  public String previousHash;
  //The timestamp of the creation of this block
  public long timestamp;
  //A nonce, which is an arbitrary number used in cryptography
  public int nonce;
  // to make sure data blocks passed between peers on a peer-to-peer network are whole, undamaged, and unaltered.
  public String merkleRoot;

  public Block(
      int nonce,
      String merkleRoot,
      String previousHash,
      long timestamp,
      Set<Transaction> transactions,
      SignaturePublicKey signaturePublicKey) {
    this.signaturePublicKey = signaturePublicKey;
    this.transactions.addAll(transactions);
    this.previousHash = previousHash;
    this.timestamp = timestamp;
    this.nonce = nonce;
    this.merkleRoot = merkleRoot;
    this.hash = calculateHash(nonce, merkleRoot, previousHash, timestamp);
  }

  protected static String calculateHash(int nonce, String merkleRoot, String previousHash, long timestamp) {
    return applySha256(nonce + merkleRoot + previousHash + timestamp);
  }

  public String getHash() {
    return hash;
  }

  public Set<Transaction> getTransactions() {
    return transactions;
  }

  public String getPreviousHash() {
    return previousHash;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public int getNonce() {
    return nonce;
  }

  public String getMerkleRoot() {
    return merkleRoot;
  }

  public SignaturePublicKey getSignaturePublicKey() {
    return signaturePublicKey;
  }

  @Override
  public String toString() {
    return "Block{" +
        "signaturePublicKey=" + signaturePublicKey +
        ", hash='" + hash + '\'' +
        ", transactions=" + transactions +
        ", previousHash='" + previousHash + '\'' +
        ", timestamp=" + timestamp +
        ", nonce=" + nonce +
        ", merkleRoot='" + merkleRoot + '\'' +
        '}';
  }

}