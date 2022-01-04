package network;

import blockchain.Block;
import blockchain.Blockchain;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import transaction.Transaction;
import transaction.UpcomingTransaction;

public interface FunctionInterface extends Remote {

  void addBlock(Block block) throws RemoteException;

  Blockchain getCurrentChain() throws RemoteException;

//  boolean signTransaction(UpcomingTransaction upcomingTransaction);

  Map<String, Double> getSystemBalance()  throws RemoteException;;

  void proposeTransactionToValidator(UpcomingTransaction upcomingTransaction)  throws RemoteException;;

  List<Transaction> getTransactionList() throws RemoteException;

  List<UpcomingTransaction> getUpcomingTransactionList() throws RemoteException;
}
