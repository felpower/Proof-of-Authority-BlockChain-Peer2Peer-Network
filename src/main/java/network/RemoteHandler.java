package network;

import blockchain.Block;
import blockchain.Blockchain;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Map;
import transaction.Transaction;
import transaction.UpcomingTransaction;

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

  @Override
  public Map<String, Double> getSystemBalance() {
    return felCoinSystem.systemBalance;
  }

  @Override
  public List<Transaction> getTransactionList() throws RemoteException {
    return felCoinSystem.transactionList;
  }

  @Override
  public List<UpcomingTransaction> getUpcomingTransactionList() throws RemoteException {
    return felCoinSystem.upcomingTransactionList;
  }

  @Override
  public void proposeTransactionToValidator(UpcomingTransaction upcomingTransaction) {
    felCoinSystem.findUpcomingTransaction(upcomingTransaction).validate();
    //ToDo: Change here because the validator does not actually have to find the Transaction he just has to validate the one he receives
  }

//  @Override
//  public boolean signTransaction(UpcomingTransaction upcomingTransaction) {
//    return felCoinSystem.signTransaction(upcomingTransaction);
//  }
}
