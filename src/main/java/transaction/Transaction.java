package transaction;

import helper.SignaturePublicKey;
import java.io.Serializable;
import java.util.Objects;

public class Transaction implements Serializable {

  private UpcomingTransaction upcomingTransaction;
  private SignaturePublicKey signaturePublicKey;

  public Transaction(UpcomingTransaction upcomingTransaction, SignaturePublicKey signaturePublicKey) {
    this.upcomingTransaction = upcomingTransaction;
    this.signaturePublicKey = signaturePublicKey;
  }

  public int getPortFromCreater(){
    return upcomingTransaction.getSender().getPort();
  }

  public UpcomingTransaction getUpcomingTransaction() {
    return upcomingTransaction;
  }

  public SignaturePublicKey getSignaturePublicKey() {
    return signaturePublicKey;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transaction that = (Transaction) o;
    return getUpcomingTransaction().equals(that.getUpcomingTransaction()) && getSignaturePublicKey().equals(that.getSignaturePublicKey());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getUpcomingTransaction(), getSignaturePublicKey());
  }

  @Override
  public String toString() {
    return "Transaction{" +
        "upcomingTransaction=" + upcomingTransaction +
        '}';
  }
}
