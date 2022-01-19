package blockchain;

import static helper.HashCode.applySha256;
import static java.util.stream.Collectors.toList;

import application.Wallet;
import blockchain.Block.Header;
import helper.SignaturePublicKey;
import java.util.Date;
import java.util.List;
import network.FelCoinSystem;

public class Miner {

  public static Block mineNewBlock(Wallet wallet, FelCoinSystem network) {
    Block lastBlockInChain = network.getBlockchain().getLastBlockInChain();
    List<String> listOfTransactionHashes = network.getTransactions().stream()
        .map(transaction -> applySha256(transaction.toString()))
        .collect(toList());
    MerkleTree merkleTree = new MerkleTree(listOfTransactionHashes);
    merkleTree.createMerkleTree();

    Header header = new Header(lastBlockInChain.getHash(),
        new Date().getTime(),
        lastBlockInChain.getNonce() + 1,
        merkleTree.getRoot());
    Block block = new Block(header, network.getTransactions(), new SignaturePublicKey(wallet.signBlock(header), wallet.getKeyPair().getPublic()));

    network.addBlock(block);
    return block;
  }
}
