package blockchain;

import static blockchain.Block.calculateHash;

import helper.SignaturePublicKey;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Blockchain implements Serializable {

  private final List<Block> blockchain = new ArrayList<>();

  public Blockchain() {
    Block genesis = new Block(0, "root", null, new Date().getTime(), Collections.emptySet(), new SignaturePublicKey());
    blockchain.add(genesis);
  }

  public boolean addBlock(Block block) {
    if (verifyBlock(block)) {
      System.out.println("Added Block: " + block);
      blockchain.add(block);
      return true;
    }
    System.out.println("Could not verify Block");
    return false;
  }

  public List<Block> getBlocks() {
    return blockchain;
  }

  protected boolean verifyBlock(Block block) {
    String hashCode = calculateHash(
        block.getNonce(),
        block.getMerkleRoot(),
        block.getPreviousHash(),
        block.getTimestamp());
    if (block.getNonce() != getLastBlockInChain().getNonce() + 1) {
      System.out.println("The new BLock is not the next in line");
      System.out.println("Last Block in chain: " + getLastBlockInChain().toString());
      return false;
    } else if (!block.getPreviousHash().equals(getLastBlockInChain().getHash())) {
      System.out.println("The new Block has the wrong previous hash");
      System.out.println("Block.previousHash: " + block.getPreviousHash() + " last blocks hash: " + getLastBlockInChain().getHash());
      return false;
    } else if (!block.getHash().equals(hashCode)) {
      System.out.println("The new Blocks calculated hash is wrong");
      System.out.println("Sent blocks hashCode: " + block.getHash() + " calculated HashCode " + hashCode);
      return false;
    }
    return true;
  }

  public Block getLastBlockInChain() {
    return blockchain.get(blockchain.size() - 1);
  }

  public void print() {
    System.out.println("Blockchain: ");
    for (int i = 0; i < blockchain.size(); i++) {
      System.out.println("Block #" + i + ": " + blockchain.get(i).toString());
    }
  }
}
