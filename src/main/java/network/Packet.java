package network;

import blockchain.Block;
import blockchain.Blockchain;
import peer.Peer;
public class Packet {

  private Block block;
  private Blockchain blockchain;
  private String action;
  private Peer peer;
  private int port;

  public Packet() {
  }

  public Packet(String action, Peer peer) {
    this.action = action;
    this.peer = peer;
  }

  public Packet(String action, Peer peer, Blockchain blockchain){
    this.action = action;
    this.peer = peer;
    this.blockchain = blockchain;
  }

  public Packet(String action, Peer peer, Block block){
    this.action = action;
    this.peer = peer;
    this.block = block;
  }

  public Packet(String action, Peer peer, int port){
    this.action = action;
    this.peer = peer;
    this.port = port;
  }

  public String getAction() {
    return action;
  }

  public Peer getPeer() {
    return peer;
  }

  public Blockchain getBlockchain() {
    return blockchain;
  }

  public Block getBlock() {
    return block;
  }

  public int getPort() {
    return port;
  }
}
