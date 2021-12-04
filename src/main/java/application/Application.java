package application;

import static application.MulticastSender.requestHandshake;
import static peer2peer.PortSender.requestChainSync;

import blockchain.BlockChain;
import blockchain.FelCoinNetwork;
import java.io.IOException;
import java.security.KeyPair;
import peer2peer.MulticastReceiver;
import peer2peer.PortReceiver;

public class Application {

  private static Wallet wallet;
  private static BlockChain blockchain;
  private static FelCoinNetwork felCoin;

  public static void launch(Node node, KeyPair keyPair, BlockChain blockChain) throws IOException {
    wallet = new Wallet(keyPair);
    blockchain = blockChain;
    felCoin = new FelCoinNetwork();

    MulticastReceiver multicastReceiver = new MulticastReceiver(node);
    multicastReceiver.setDaemon(true);
    multicastReceiver.start();

    PortReceiver portListener = new PortReceiver(node);
    portListener.setDaemon(true);
    portListener.start();

    requestHandshake(node);
    requestChainSync(node, blockchain.getLastBlockInChain());
  }

  public static Wallet getWallet() {
    return wallet;
  }

  public static BlockChain getBlockchain() {
    return blockchain;
  }

  public static FelCoinNetwork getFelCoin() {
    return felCoin;
  }
}
