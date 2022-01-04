package network;

import static helper.RandomHelper.getRandomPeer;
import static java.lang.String.valueOf;
import static java.rmi.Naming.lookup;
import static java.util.Collections.singletonList;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import transaction.Transaction;
import transaction.UpcomingTransaction;

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
  public Blockchain getCurrentChain(Peer peer) {
    try {
      List<Peer> peers = getPeers();
      if (peers.size() <= 1) {
        System.out.println("Initialised new Chain");
        return new Blockchain();
      }
      FunctionInterface remote = (FunctionInterface) lookup(valueOf(getRandomPeer(peers, singletonList(peer)).getUrl()));
      Blockchain chain = remote.getCurrentChain();
      System.out.println("Blockchain found from other Peer: ");
      chain.print();
      return chain;
    } catch (NotBoundException | MalformedURLException | RemoteException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public Map<String, Double> getSystemBalance(Peer peer) {
    try {
      List<Peer> peers = getPeers();
      if (peers.size() <= 1) {
        System.out.println("The System has now a Balance of 100 Coins from Peer: " + peer.toString());

        HashMap<String, Double> map = new HashMap<>();
        map.put(peer.getAddress(), 100.);
        return map;
      }
      FunctionInterface remote = (FunctionInterface) lookup(valueOf(getRandomPeer(peers, singletonList(peer)).getUrl()));
      Map<String, Double> systemBalance = remote.getSystemBalance();
      System.out.println("Balance of System is currently: " + systemBalance);
      return systemBalance;
    } catch (NotBoundException | MalformedURLException | RemoteException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public List<Transaction> getTransactionList(Peer peer) {
    try {
      List<Peer> peers = getPeers();
      if (peers.size() <= 1) {
        return new ArrayList<>();
      }
      FunctionInterface remote = (FunctionInterface) lookup(valueOf(getRandomPeer(peers, singletonList(peer)).getUrl()));
      return remote.getTransactionList();
    } catch (NotBoundException | MalformedURLException | RemoteException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public List<UpcomingTransaction> getUpcomingTransactionList(Peer peer) {
    try {
      List<Peer> peers = getPeers();
      if (peers.size() <= 1) {
        return new ArrayList<>();
      }
      FunctionInterface remote = (FunctionInterface) lookup(valueOf(getRandomPeer(peers, singletonList(peer)).getUrl()));
      return remote.getUpcomingTransactionList();
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

//  @Override
//  public boolean signTransaction(UpcomingTransaction upcomingTransaction) {
//    try {
//      FunctionInterface remote = (FunctionInterface) lookup(upcomingTransaction.getSender().getUrl());
//      return remote.signTransaction(upcomingTransaction);
//    } catch (NotBoundException | MalformedURLException | RemoteException e) {
//      e.printStackTrace();
//    }
//    return false;
//  }

  @Override
  public void proposeTransactionToValidators(UpcomingTransaction upcomingTransaction, Peer except) {
    try {
      for (Peer peer : getPeers()) {
        if (peer.equals(except)) {
          continue;
        }
        FunctionInterface remote = (FunctionInterface) lookup(peer.getUrl());
        remote.proposeTransactionToValidator(upcomingTransaction);
      }
    } catch (NotBoundException | MalformedURLException | RemoteException e) {
      e.printStackTrace();
    }
  }

}
