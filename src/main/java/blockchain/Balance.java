package blockchain;

public class Balance {

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
