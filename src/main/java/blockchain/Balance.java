package blockchain;

import java.io.Serializable;

public class Balance implements Serializable {

  private final String name;
  private final double amount;

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
}
