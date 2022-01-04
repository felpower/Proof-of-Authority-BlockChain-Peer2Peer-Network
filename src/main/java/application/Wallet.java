package application;

import static helper.HashCode.applySha256;
import static java.security.Signature.getInstance;
import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

import blockchain.Balance;
import blockchain.Block.Header;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.Signature;
import transaction.UpcomingTransaction;

public class Wallet implements Serializable {

  // Private and Public Key Pair
  private final KeyPair keyPair;
  // The address of the Walled
  private final String address;
  // The Current balance of the Wallet
  private Balance balance;

  public Wallet(KeyPair keyPair) {
    this.keyPair = keyPair;
    this.address = applySha256(new String(keyPair.getPublic().getEncoded()));
    this.balance = new Balance(100);
  }

  public byte[] signKey(Header header) {
    try {
      Signature signature = getInstance("ECDSA", PROVIDER_NAME);
      signature.initSign(keyPair.getPrivate());
      signature.update(header.toString().getBytes());
      return signature.sign();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public byte[] signTransaction(UpcomingTransaction upcomingTransaction) {
    try {
      Signature signature = getInstance("ECDSA", PROVIDER_NAME);
      signature.initSign(keyPair.getPrivate());
      signature.update(upcomingTransaction.toString().getBytes());
      return signature.sign();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public KeyPair getKeyPair() {
    return keyPair;
  }

  public String getAddress() {
    return address;
  }

  public Balance getBalance() {
    return balance;
  }
}