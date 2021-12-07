package blockchain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import transaction.Transaction;

public class FelCoinNetwork {

  private final Map<String, Float> coins;
  private final List<Transaction> transactionList;

  public FelCoinNetwork() {
    this.coins = new HashMap<>();
    this.transactionList = new ArrayList<>();
  }

  public Map<String, Float> getCoins() {
    return coins;
  }

  public List<Transaction> getTransactionList() {
    return transactionList;
  }
}
