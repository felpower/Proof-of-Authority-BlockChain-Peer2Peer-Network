import java.util.Date;

public class Block {

  //The hash of this block, calculated based on other data
  public String hash;
  //Hash of the previous block, an important part to build the chain
  public String previousHash;
  //The actual data, any information having value, like a contract
  public String data;
  //The timestamp of the creation of this block
  public long timeStamp;
  //A nonce, which is an arbitrary number used in cryptography
  public int nonce;

  public Block(String data, String previousHash) {
    this.data = data;
    this.previousHash = previousHash;
    this.timeStamp = new Date().getTime();
    this.hash = calculateHash();
  }

  public String calculateHash() {
    return StringUtil.applySha256(
        previousHash +
            timeStamp +
            nonce +
            data
    );
  }

  public void mineBlock(int difficulty) {
    String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
    while (!hash.substring(0, difficulty).equals(target)) {
      nonce++;
      hash = calculateHash();
    }
    System.out.println("Block Mined!!! : " + hash);
  }

}