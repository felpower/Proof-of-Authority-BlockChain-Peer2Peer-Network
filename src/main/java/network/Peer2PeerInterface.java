package network;

import application.Peer;
import blockchain.Block;
import blockchain.Blockchain;
import java.util.List;

public interface Peer2PeerInterface {

  public Blockchain getCurrentChain();

  public List<Peer> getPeers();

  public void sendBlock(Block block);

  public boolean removePeer(Peer peer);
}
