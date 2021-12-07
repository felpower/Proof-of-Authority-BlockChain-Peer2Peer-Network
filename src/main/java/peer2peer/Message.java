package peer2peer;

import application.Node;
import com.fasterxml.jackson.annotation.JsonInclude;
import helper.SignaturePublicKey;
import java.util.List;
import transaction.Transaction;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message<T> {

  private MessageType messageType;
  private Node sender;
  private T payload;
  private List<Transaction> transactionList;
  private SignaturePublicKey signaturePublicKey;

  public Message() {
// deserializer for Jackson
  }

  public Message(MessageType messageType, Node sender, T payload) {
    this.messageType = messageType;
    this.sender = sender;
    this.payload = payload;
    this.transactionList = null;
    this.signaturePublicKey = null;
  }

  public MessageType getMessageType() {
    return messageType;
  }

  public Node getSender() {
    return sender;
  }

  public T getPayload() {
    return payload;
  }

  public List<Transaction> getTransactionList() {
    return transactionList;
  }

  public SignaturePublicKey getSignaturePublicKey() {
    return signaturePublicKey;
  }
}
