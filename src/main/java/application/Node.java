package application;

import static helper.HashCode.applySha256;
import static java.util.Objects.hash;

import java.security.KeyPair;

public class Node {

  private int port;
  private String username;
  private String address;

  public Node() {
    // deserializer for Jackson
  }

  public Node(int port, String username, KeyPair keyPair) {
    this.port = port;
    this.username = username;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Node node = (Node) o;
    return getPort() == node.getPort() && getUsername().equals(node.getUsername()) && getAddress().equals(node.getAddress());
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
