package transaction;

import helper.SignaturePublicKey;
import java.io.Serializable;

public class Transaction implements Serializable {

  private UpcomingTransaction upcomingTransaction;
  private SignaturePublicKey signaturePublicKey;

  public Transaction(UpcomingTransaction upcomingTransaction, SignaturePublicKey signaturePublicKey) {
    this.upcomingTransaction = upcomingTransaction;
    this.signaturePublicKey = signaturePublicKey;
  }
}
