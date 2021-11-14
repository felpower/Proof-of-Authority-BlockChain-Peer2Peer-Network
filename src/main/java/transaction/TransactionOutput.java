package transaction;

import static helper.StringUtil.applySha256;
import static helper.StringUtil.getStringFromKey;

import java.security.PublicKey;

public class TransactionOutput {

  public String id;
  public PublicKey reciepient; //also known as the new owner of these coins.
  public float value; //the amount of coins they own
  public String parentTransactionId; //the id of the transaction this output was created in

  //Constructor
  public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
    this.reciepient = reciepient;
    this.value = value;
    this.parentTransactionId = parentTransactionId;
    this.id = applySha256(getStringFromKey(reciepient) + value + parentTransactionId);
  }

  //Check if coin belongs to you
  public boolean isMine(PublicKey publicKey) {
    return (publicKey == reciepient);
  }
}
