package helper;

import static java.security.Signature.getInstance;
import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Arrays;

public class SignaturePublicKey implements Serializable {

  private byte[] signature;
  private byte[] publicKeyEncoded;

  public SignaturePublicKey() {
  }

  public SignaturePublicKey(byte[] signature, PublicKey publicKey) {
    this.signature = signature;
    this.publicKeyEncoded = publicKey.getEncoded();
  }

  public byte[] getSignature() {
    return signature;
  }

  public byte[] getPublicKeyEncoded() {
    return publicKeyEncoded;
  }


//  public static boolean signTransaction(UpcomingTransaction upcomingTransaction, KeyPair keyPair) {
//    try {
//      Signature signature = getInstance("ECDSA", PROVIDER_NAME);
//      signature.initSign(keyPair.getPrivate());
//      signature.update(upcomingTransaction.toString().getBytes());
//      return signature.verify(upcomingTransaction.getSignaturePublicKey().signature);
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return false;
//  }

  @Override
  public String toString() {
    return "SignaturePublicKey{" +
        "signature=" + Arrays.toString(signature) +
        ", publicKeyEncoded=" + Arrays.toString(publicKeyEncoded) +
        '}';
  }
}
