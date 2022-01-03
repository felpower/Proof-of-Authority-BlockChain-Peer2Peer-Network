package blockchain;

import helper.HashCode;
import helper.SignaturePublicKey;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Block implements Serializable {

  // The signature and its belonging public key
  private final SignaturePublicKey signaturePublicKey;
  //The hash of this block, calculated based on other data
  public String hash;
  //The list of Transactions
  public List<String> transactions;

  // Data to make the Block unique
  public Header header;

  public Block(
      int nonce,
      String merkleRoot,
      String previousHash,
      long timestamp,
      List<String> transactions,
      SignaturePublicKey signaturePublicKey) {
    this.header = new Header(previousHash, timestamp, nonce, merkleRoot);
    this.signaturePublicKey = signaturePublicKey;
    this.transactions = transactions;
    this.hash = calculateHash(nonce, merkleRoot, previousHash, timestamp);
  }

  public Block(
      Header header,
      List<String> transactions,
      SignaturePublicKey signaturePublicKey) {
    this.header = new Header(header.previousHash, header.timestamp, header.nonce, header.merkleRoot);
    this.signaturePublicKey = signaturePublicKey;
    this.transactions = transactions;
    this.hash = calculateHash(header.nonce, header.merkleRoot, header.previousHash, header.timestamp);
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


  public Header getHeader() {
    return header;
  }

  @Override
  public String toString() {
    return "Block{" +
        "hash='" + hash + '\'' +
        ", transactions=" + transactions +
        ", header=" + header +
        '}';
  }

  public static class Header implements Serializable{

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

    @Override
    public String toString() {
      return "Header{" +
          "previousHash='" + previousHash + '\'' +
          ", timestamp=" + new Date(timestamp) +
          ", nonce=" + nonce +
          ", merkleRoot='" + merkleRoot + '\'' +
          '}';
    }
  }

}