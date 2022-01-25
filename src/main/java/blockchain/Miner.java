package blockchain;

import application.Wallet;
import helper.SignaturePublicKey;
import java.util.Date;
import network.FelCoinSystem;

public class Miner {

  public static Block mineNewBlock(Wallet wallet, FelCoinSystem network) {
    Block lastBlockInChain = network.getBlockchain().getLastBlockInChain();
    MerkleTree merkleTree = new MerkleTree(network.getTransactionHashes());

    String signingBlock = lastBlockInChain.getHash() + new Date().getTime() + lastBlockInChain.getNonce() + 1 + merkleTree.getRoot();
    SignaturePublicKey signaturePublicKey = new SignaturePublicKey(wallet.signBlock(signingBlock), wallet.getKeyPair().getPublic());
    return new Block(
        lastBlockInChain.getNonce() + 1,
        merkleTree.getRoot(),
        lastBlockInChain.getHash(),
        new Date().getTime(),
        network.getTransactionSet(),
        signaturePublicKey);
  }
}
