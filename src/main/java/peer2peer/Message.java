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

  public Message(MessageType msgType, Node sender, T payload) {
    this.messageType = msgType;
    this.sender = sender;
    this.payload = payload;
    this.transactionList = null;
    this.signaturePublicKey = null;
  }

  public Message(MessageType msgType, Node sender, T payload, List<Transaction> txs) {
    this.messageType = msgType;
    this.sender = sender;
    this.payload = payload;
    this.transactionList = txs;
    this.signaturePublicKey = null;
  }

  public Message(MessageType msgType, Node sender, T payload, SignaturePublicKey sigKey) {
    this.messageType = msgType;
    this.sender = sender;
    this.payload = payload;
    this.transactionList = null;
    this.signaturePublicKey = sigKey;
  }

  public Message(MessageType newBlock, Node orderer, T header, List<Transaction> txs, SignaturePublicKey ordererSig) {
    this.messageType = newBlock;
    this.sender = orderer;
    this.payload = header;
    this.transactionList = txs;
    this.signaturePublicKey = ordererSig;
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
