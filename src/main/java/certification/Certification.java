package certification;

import static java.security.KeyPairGenerator.getInstance;
import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

public class Certification {

  public KeyPair generateKeyPair() {
    try {
      KeyPairGenerator keyGen = getInstance("ECDSA", PROVIDER_NAME);//Elliptic Curve Digital Signature Algorithm
      SecureRandom random = new SecureRandom();
      ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
      keyGen.initialize(ecSpec, random);
      return keyGen.generateKeyPair();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
