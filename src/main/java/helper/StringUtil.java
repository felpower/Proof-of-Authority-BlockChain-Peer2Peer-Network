package helper;

import static java.lang.Integer.toHexString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.MessageDigest.getInstance;
import static java.util.Base64.getEncoder;

import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import transaction.Transaction;

public class StringUtil {

  //

  /**
   * Applies the Sha256 hashing algorithm to a string and returns the result.
   * @param input The input which we want to apply the Sha256 hashing algorithm to.
   * @return the hashed String of the input
   */
  public static String applySha256(String input) {
    try {
      MessageDigest digest = getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(UTF_8));
      StringBuilder hexString = new StringBuilder();
      for (byte b : hash) {
        String hex = toHexString(0xff & b);
        if (hex.length() == 1) {
          hexString.append('0');
        }
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getDificultyString(int difficulty) {
    return new String(new char[difficulty]).replace('\0', '0');
  }

  /**
   * @param privateKey takes in the senders private key
   * @param input takes in the string key from the sender and recipient plus the sent value
   * @return Applies ECDSA Signature and returns the result ( as bytes ).
   */
  public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
    Signature dsa;
    byte[] output;
    try {
      dsa = Signature.getInstance("ECDSA", "BC");
      dsa.initSign(privateKey);
      byte[] strByte = input.getBytes();
      dsa.update(strByte);
      output = dsa.sign();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return output;
  }

  //Verifies a String signature

  /**
   * @param publicKey takes in the senders private key.
   * @param data takes in the string key from the sender and recipient plus the amount to send.
   * @param signature this should be the same as the data, Verifies the data we signed has not been tampered with.
   * @return true if everything is OK and false if the data has been tampered with.
   */
  public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
    try {
      Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
      ecdsaVerify.initVerify(publicKey);
      ecdsaVerify.update(data.getBytes());
      return ecdsaVerify.verify(signature);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String getStringFromKey(Key key) {
    return getEncoder().encodeToString(key.getEncoded());
  }

  //Tacks in array of transactions and returns a merkle root.
  public static String getMerkleRoot(ArrayList<Transaction> transactions) {
    int count = transactions.size();
    ArrayList<String> previousTreeLayer = new ArrayList<>();
    for (Transaction transaction : transactions) {
      previousTreeLayer.add(transaction.transactionId);
    }
    ArrayList<String> treeLayer = previousTreeLayer;
    while (count > 1) {
      treeLayer = new ArrayList<>();
      for (int i = 1; i < previousTreeLayer.size(); i++) {
        treeLayer.add(applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
      }
      count = treeLayer.size();
      previousTreeLayer = treeLayer;
    }
    return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
  }
}