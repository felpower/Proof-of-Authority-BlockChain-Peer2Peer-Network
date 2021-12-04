package application;

import static helper.HashCode.applySha256;

import java.security.KeyPair;

public class Node {

  private int port;
  private String username;
  private String address;

  public Node(int port, String username, KeyPair keyPair)
  {
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
}
