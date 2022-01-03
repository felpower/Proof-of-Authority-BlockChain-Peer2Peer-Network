package network;

import blockchain.Block;
import blockchain.Blockchain;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FunctionInterface extends Remote {

  public void addBlock(Block block) throws RemoteException;

  public Blockchain getCurrentChain() throws RemoteException;
}
