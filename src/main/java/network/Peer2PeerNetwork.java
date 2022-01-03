package network;

import static java.rmi.Naming.lookup;
import static java.util.Objects.isNull;

import application.Peer;
import blockchain.Block;
import blockchain.Blockchain;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

public class Peer2PeerNetwork implements Peer2PeerInterface {

  private static final String PEERS_FILE = "/felcoinnetwork-peers.ser";
  private final FelCoinSystem felCoinSystem;

  public Peer2PeerNetwork(FelCoinSystem felCoinSystem, Peer peer) {
    this.felCoinSystem = felCoinSystem;
    try {
      startHandler(peer);
      registerPort(peer);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }

  private void startHandler(final Peer peer) throws RemoteException {
    RemoteHandler remoteHandler = new RemoteHandler(felCoinSystem);
    Registry registry = LocateRegistry.createRegistry(peer.getPort());
    registry.rebind("felcoinnetwork", remoteHandler);
  }

  private void registerPort(Peer peer) {
    List<Peer> peers = getPeers();
    if (!peers.contains(peer)) {
      peers.add(peer);
      saveNewPeerList(peers);
    }
  }

  private void saveNewPeerList(List<Peer> peers) {
    try {
      File yourFile = new File(PEERS_FILE);
      boolean newFile = yourFile.createNewFile();// if file already exists will do nothing
      if (newFile) {
        System.out.println("New File Created");
      }
      FileOutputStream fileOut = new FileOutputStream(yourFile);
      ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
      objectOut.writeObject(peers);
      objectOut.close();

    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Blockchain getCurrentChain() {
    try {
      List<Peer> peers = getPeers();
      FunctionInterface remote = (FunctionInterface) lookup(String.valueOf(peers.get(0).getUrl()));

      Blockchain chain = remote.getCurrentChain();
      if (isNull(chain)) {
        System.out.println("Initialised new Chain");
        return new Blockchain();
      } else {
        System.out.println("Blockchain found from other Peer: ");
        chain.print();
        return chain;
      }

    } catch (NotBoundException | MalformedURLException | RemoteException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public void sendBlock(Block block) {
    List<Peer> peers = getPeers();
    for (Peer peer : peers) {
      try {
        FunctionInterface remote = (FunctionInterface) lookup(peer.getUrl());
        remote.addBlock(block);
      } catch (RemoteException ex) {
        ex.printStackTrace();
      } catch (NotBoundException | MalformedURLException ex) {
        throw new RuntimeException(ex);
      }
    }
  }

  @Override
  public List<Peer> getPeers() {
    try {
      new File(PEERS_FILE);
      FileInputStream file = new FileInputStream(PEERS_FILE);
      ObjectInputStream objectIn = new ObjectInputStream(file);
      List<Peer> peers = (List<Peer>) objectIn.readObject();
      objectIn.close();
      return peers;
    } catch (FileNotFoundException ex) {
      return new LinkedList<>();
    } catch (IOException | ClassNotFoundException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public boolean removePeer(Peer peer) {
    List<Peer> peers = getPeers();
    boolean removed = peers.remove(peer);
    if (!removed) {
      System.out.println("Could not remove Peer: " + peer);
      return false;
    }
    saveNewPeerList(peers);
    System.out.println("Removed Peer: " + peer);
    return true;
  }
}
