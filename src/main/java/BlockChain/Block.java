package BlockChain;

import helper.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import transaction.Transaction;

public class Block {

  //The hash of this block, calculated based on other data
  public String hash;
  //Hash of the previous block, an important part to build the chain
  public String previousHash;
  //The timestamp of the creation of this block
  public long timeStamp;
  //A nonce, which is an arbitrary number used in cryptography
  public int nonce;

  public String merkleRoot;
  public ArrayList<Transaction> transactions = new ArrayList<>(); //our data will be a simple message.

  //Block Constructor.
  public Block(String previousHash) {
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();

    this.hash = calculateHash(); //Making sure we do this after we set the other values.
  }

  //Calculate new hash based on blocks contents
  public String calculateHash() {
    return StringUtil.applySha256(
        previousHash +
            timeStamp +
            nonce +
            merkleRoot
    );
  }

  //Increases nonce value until hash target is reached.
  public void mineBlock(int difficulty) {
    merkleRoot = StringUtil.getMerkleRoot(transactions);
    String target = StringUtil.getDificultyString(difficulty); //Create a string with difficulty * "0"
    while (!hash.substring(0, difficulty).equals(target)) {
      nonce++;
      hash = calculateHash();
    }
    System.out.println("Block Mined!!! : " + hash);
  }

  //Add transactions to this block
  public void addTransaction(Transaction transaction) {
    //process transaction and check if valid, unless block is genesis block then ignore.
    if (transaction == null) {
      return;
    }
    if (!Objects.equals(previousHash, "0")) {
      if ((!transaction.processTransaction())) {
        System.out.println("Transaction failed to process. Discarded.");
        return;
      }
    }
    transactions.add(transaction);
    System.out.println("Transaction Successfully added to Block");
  }

}