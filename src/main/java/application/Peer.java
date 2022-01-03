package application;

import static helper.HashCode.applySha256;
import static java.util.Objects.hash;

import java.io.Serializable;
import java.security.KeyPair;

public class Peer implements Serializable {

  private int port;
  private String username;
  private String address;
  private KeyPair keyPair;

  public Peer() {
    // deserializer for Jackson
  }

  public Peer(int port, String username, KeyPair keyPair) {
    this.port = port;
    this.username = username;
    this.keyPair = keyPair;
    this.address = applySha256(new String(keyPair.getPublic().getEncoded()));
  }

  public int getPort() {
    return port;
  }

  public String getUsername() {
    return username;
  }

  public String getAddress() {
    return address;
  }

  public KeyPair getKeyPair() {
    return keyPair;
  }

  public String getUrl() {
    return "rmi://127.0.0.1:" + port + "/felcoinnetwork";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Peer peer = (Peer) o;
    return getPort() == peer.getPort() && getUsername().equals(peer.getUsername()) && getAddress().equals(peer.getAddress());
  }

  @Override
  public int hashCode() {
    return hash(getPort(), getUsername(), getAddress());
  }

  @Override
  public String toString() {
    return "Node{" +
        "port=" + port +
        ", username='" + username + '\'' +
        ", address='" + address + '\'' +
        '}';
  }
}
