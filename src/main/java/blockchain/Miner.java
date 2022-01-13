package blockchain;

import application.Wallet;
import blockchain.Block.Header;
import helper.SignaturePublicKey;
import java.util.Collections;
import java.util.Date;

public class Miner {

  public static Block mineNewBlock(Wallet wallet, Blockchain blockchain) {
    Block lastBlockInChain = blockchain.getLastBlockInChain();
    Header header = new Header(lastBlockInChain.getHash(), new Date().getTime(), lastBlockInChain.getNonce() + 1, "root");
    Block block = new Block(header, Collections.emptyList(), new SignaturePublicKey(wallet.signKey(header), wallet.getKeyPair().getPublic()));

    blockchain.addBlock(block);
    return block;//ToDo: Merkle Root and List of Transactions
  }
}
