package blockchain;

public enum Balance
{
    FLC("FelCoin", 1000);

    private String name;
    private double price;

    Balance(String name, double value)
    {
        this.name = name;
        this.price = value;
    }

}
