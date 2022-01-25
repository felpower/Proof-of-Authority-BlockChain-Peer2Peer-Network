package blockchain;

import static helper.HashCode.applySha256;

import java.util.ArrayList;
import java.util.List;

/*
https://github.com/goyalnikhil02/MerkleTree/blob/master/src/com/example/MerkleTrees.java
 */
public class MerkleTree {

  private String root;

  public MerkleTree(List<String> transactionList) {
    createMerkleTree(transactionList);
  }

  public MerkleTree createMerkleTree(List<String> transactionList) {
    List<String> merkledList = getTransactionList(new ArrayList<>(transactionList));
    while (merkledList.size() != 1) {
      merkledList = getTransactionList(merkledList);
    }

    this.root = merkledList.get(0);
    return this;
  }

  private List<String> getTransactionList(List<String> tempTxList) {
    List<String> newTxList = new ArrayList<>();
    int index = 0;
    while (index < tempTxList.size()) {
      String left = tempTxList.get(index);
      index++;

      String right = "";
      if (index != tempTxList.size()) {
        right = tempTxList.get(index);
      }

      newTxList.add(applySha256(left + right));
      index++;
    }

    return newTxList;
  }

  public String getRoot() {
    return this.root;
  }

}
