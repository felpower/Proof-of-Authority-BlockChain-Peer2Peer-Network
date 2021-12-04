package helper;

import java.security.PublicKey;

public class SignaturePublicKey {

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
