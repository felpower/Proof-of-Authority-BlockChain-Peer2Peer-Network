package blockchain;

import static helper.HashCode.applySha256;

import java.util.ArrayList;
import java.util.List;

/*
https://github.com/goyalnikhil02/MerkleTree/blob/master/src/com/example/MerkleTrees.java
 */
public class MerkleTree {

  private final List<String> transactionList;
  private String root;

  public MerkleTree(List<String> txList) {
    this.transactionList = txList;
  }

  public void createMerkleTree() {
    List<String> merkledList = getNewTxList(new ArrayList<>(this.transactionList));
    while (merkledList.size() != 1) {
      merkledList = getNewTxList(merkledList);
    }

    this.root = merkledList.get(0);
  }

  private List<String> getNewTxList(List<String> tempTxList) {
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
