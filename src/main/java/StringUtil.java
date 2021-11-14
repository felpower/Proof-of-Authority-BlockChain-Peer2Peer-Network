import static java.lang.Integer.toHexString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.MessageDigest.getInstance;

import com.google.gson.GsonBuilder;
import java.security.MessageDigest;

public class StringUtil {

  //takes a string and applies SHA256 algorithm to it
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

  //Short hand helper to turn Object into a json string
  public static String getJson(Object o) {
    return new GsonBuilder().setPrettyPrinting().create().toJson(o);
  }

  //Returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
  public static String getDificultyString(int difficulty) {
    return new String(new char[difficulty]).replace('\0', '0');
  }
}
