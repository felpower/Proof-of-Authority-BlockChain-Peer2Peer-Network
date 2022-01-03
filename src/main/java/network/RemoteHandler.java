package network;

import blockchain.Block;
import blockchain.Blockchain;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteHandler extends UnicastRemoteObject implements FunctionInterface {

  private final FelCoinSystem felCoinSystem;

  public RemoteHandler(FelCoinSystem felCoinSystem) throws RemoteException {
    super();
    this.felCoinSystem = felCoinSystem;
  }


  @Override
  public void addBlock(Block block) throws RemoteException {
    felCoinSystem.addBlock(block);
  }

  @Override
  public Blockchain getCurrentChain() throws RemoteException {
    return felCoinSystem.blockchain;
  }
}
