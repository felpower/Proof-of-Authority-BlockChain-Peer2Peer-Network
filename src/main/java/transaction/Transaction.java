package transaction;

import static helper.StringUtil.applyECDSASig;
import static helper.StringUtil.applySha256;
import static helper.StringUtil.getStringFromKey;
import static helper.StringUtil.verifyECDSASig;

import BlockChain.FelCoin;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {

  public String transactionId; // this is also the hash of the transaction.
  public PublicKey sender; // senders address/public key.
  public PublicKey reciepient; // Recipients address/public key.
  public float value;
  public byte[] signature; // this is to prevent anybody else from spending funds in our wallet.

  public ArrayList<TransactionInput> inputs;
  public ArrayList<TransactionOutput> outputs = new ArrayList<>();

  private static int sequence = 0; // a rough count of how many transactions have been generated.

  public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
    this.sender = from;
    this.reciepient = to;
    this.value = value;
    this.inputs = inputs;
  }

  // This Calculates the transaction hash (which will be used as its Id)
  private String calulateHash() {
    sequence++; //increase the sequence to avoid 2 identical transactions having the same hash
    return applySha256(
        getStringFromKey(sender) +
            getStringFromKey(reciepient) +
            value + sequence
    );
  }

  //Signs all the data we don't wish to be tampered with.
  public void generateSignature(PrivateKey privateKey) {
    signature = applyECDSASig(privateKey, getData());
  }

  //Verifies the data we signed has not been tampered with
  public boolean verifiySignature() {
    return verifyECDSASig(sender, getData(), signature);
  }

  public String getData() {
    return getStringFromKey(sender) + getStringFromKey(reciepient) + value;
  }

  public boolean processTransaction() {
    if (!verifiySignature()) {
      System.out.println("#Transaction Signature failed to verify");
      return false;
    }

    //gather transaction inputs (Make sure they are unspent):
    for (TransactionInput i : inputs) {
      i.UTXO = FelCoin.UTXOs.get(i.transactionOutputId);
    }

    //check if transaction is valid:
    if (getInputsValue() < FelCoin.minimumTransaction) {
      System.out.println("#Transaction Inputs to small: " + getInputsValue());
      return false;
    }

    //generate transaction outputs:
    float leftOver = getInputsValue() - value; //get value of inputs then the left over change:
    transactionId = calulateHash();
    outputs.add(new TransactionOutput(this.reciepient, value, transactionId)); //send value to recipient
    outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); //send the left over 'change' back to sender

    //add outputs to Unspent list
    for (TransactionOutput o : outputs) {
      FelCoin.UTXOs.put(o.id, o);
    }

    //remove transaction inputs from UTXO lists as spent:
    for (TransactionInput i : inputs) {
      if (i.UTXO == null) {
        continue; //if Transaction can't be found skip it
      }
      FelCoin.UTXOs.remove(i.UTXO.id);
    }

    return true;
  }

  //returns sum of inputs(UTXOs) values
  public float getInputsValue() {
    float total = 0f;
    for (TransactionInput i : inputs) {
      if (i.UTXO == null) {
        continue; //if Transaction can't be found skip it
      }
      total += i.UTXO.value;
    }
    return total;
  }

  //returns sum of outputs:
  public float getOutputsValue() {
    float total = 0f;
    for (TransactionOutput o : outputs) {
      total += o.value;
    }
    return total;
  }
}
