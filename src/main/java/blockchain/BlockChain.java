package blockchain;

import helper.SignaturePublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockChain {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  public static List<Block> blockchain = new ArrayList<>();

  public BlockChain() {
    Block genesis = new Block(0, "Genesis", null, new Date().getTime(), new ArrayList<>(), new SignaturePublicKey());
    blockchain.add(genesis);
  }

  public boolean addBlock(Block b) {
    if (verifyBlock(b)) {
      blockchain.add(b);
      logger.info("Added new {} to block chain.", b);
      return true;
    }
    return false;
  }

  protected boolean verifyBlock(Block newBlock) {
    String hashCode = Block.calculateHash(newBlock.getNonce(),
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
}
