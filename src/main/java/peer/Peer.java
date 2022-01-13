package peer;

import static helper.HashCode.applySha256;
import static java.util.Objects.hash;

import java.io.IOException;
import java.io.Serializable;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Peer implements Serializable {

  private int port;
  private String username;
  private String address;
  private final Set<Role> roleList = new HashSet<>();

  public Peer() {
    // deserializer for Jackson
  }

  public Peer(int port, String username, KeyPair keyPair, Role role) {
    this.port = port;
    this.username = username;
    this.address = applySha256(new String(keyPair.getPublic().getEncoded()));
    this.roleList.add(role);
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

  public Set<Role> getRoles() {
    return roleList;
  }

  public boolean hasRole(Role role) {
    return roleList.contains(role);
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
    return "Peer{" +
        "port=" + port +
        ", username='" + username + '\'' +
        ", address='" + address + '\'' +
        ", roleList=" + roleList +
        '}';
  }

  public Peer addRole(Role role) {
    this.roleList.add(role);
    return this;
  }

  public boolean hasPort(int port) {
    return this.port == port;
  }

  public boolean hasAddress(String address) {
   return this.address.equals(address);
  }
}
