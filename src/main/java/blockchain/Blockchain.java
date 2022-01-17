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

  public boolean addBlock(Block b) {
    if (verifyBlock(b)) {
      blockchain.add(b);
      return true;
    }
    System.out.println("Could not verify Block");
    return false;
  }

  public List<Block> getBlocks() {
    return blockchain;
  }

  protected boolean verifyBlock(Block newBlock) {
    String hashCode = calculateHash(newBlock.getNonce(),
        newBlock.getMerkleRoot(),
        newBlock.getPreviousHash(),
        newBlock.getTimestamp());

    return
        newBlock.getNonce() == getLastBlockInChain().getNonce() + 1
            && newBlock.getPreviousHash().equals(getLastBlockInChain().getHash())
            && newBlock.getHash().equals(hashCode);
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
