package transaction;

import helper.SignaturePublicKey;

public class Transaction {

  private UpcomingTransaction upcomingTransaction;
  private SignaturePublicKey signaturePublicKey;

  public Transaction(UpcomingTransaction upcomingTransaction, SignaturePublicKey signaturePublicKey) {
    this.upcomingTransaction = upcomingTransaction;
    this.signaturePublicKey = signaturePublicKey;
  }
}
