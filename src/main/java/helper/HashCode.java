package helper;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.security.MessageDigest.getInstance;
import static org.bouncycastle.util.encoders.Hex.encode;

import java.security.MessageDigest;

public class HashCode {

  public static String applySha256(String input) {
    try {
      MessageDigest digest = getInstance("SHA-256");
      byte[] hash = digest.digest(input.getBytes(UTF_8));
      return new String(encode(hash));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
