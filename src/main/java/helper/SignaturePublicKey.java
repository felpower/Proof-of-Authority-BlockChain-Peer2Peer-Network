package helper;

import java.io.Serializable;
import java.security.PublicKey;
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

}
