package blockchain;

import java.io.Serializable;

public class Balance implements Serializable {

  private final String name;
  private double amount;

  public Balance(double startingValue) {
    this.name = "Felcoin";
    this.amount = startingValue;
  }

  public String getName() {
    return name;
  }

  public double getAmount() {
    return amount;
  }

  public void addOrRemoveCoins(double amount) {
    this.amount = this.amount + amount;
  }
}
