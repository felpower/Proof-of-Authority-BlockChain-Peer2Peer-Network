package helper;

import static java.security.Signature.getInstance;
import static org.bouncycastle.jce.provider.BouncyCastleProvider.PROVIDER_NAME;

import java.io.Serializable;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import transaction.UpcomingTransaction;

public class SignaturePublicKey implements Serializable {

  private byte[] signature;
  private byte[] publicKeyEncoded;

  public SignaturePublicKey() {
  }

  public SignaturePublicKey(byte[] signature, PublicKey publicKey) {
    this.signature = signature;
    this.publicKeyEncoded = publicKey.getEncoded();
  }

  public static boolean verify(UpcomingTransaction upcomingTransaction) {
    try {
      Signature signature = getInstance("ECDSA", PROVIDER_NAME);
      PublicKey publicKey = KeyFactory.getInstance("ECDSA", PROVIDER_NAME)
          .generatePublic(new X509EncodedKeySpec(upcomingTransaction.getPublicKeyEncoded()));
      signature.initVerify(publicKey);
      signature.update(upcomingTransaction.toString().getBytes());
      return signature.verify(upcomingTransaction.getSignaturePublicKey().signature);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public byte[] getSignature() {
    return signature;
  }

  public byte[] getPublicKeyEncoded() {
    return publicKeyEncoded;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SignaturePublicKey that = (SignaturePublicKey) o;
    return Arrays.equals(getSignature(), that.getSignature()) && Arrays.equals(getPublicKeyEncoded(), that.getPublicKeyEncoded());
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(getSignature());
    result = 31 * result + Arrays.hashCode(getPublicKeyEncoded());
    return result;
  }

  @Override
  public String toString() {
    return "SignaturePublicKey{" +
        "signature=" + Arrays.toString(signature) +
        ", publicKeyEncoded=" + Arrays.toString(publicKeyEncoded) +
        '}';
  }
}
