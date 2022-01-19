package peer;

import static helper.HashCode.applySha256;
import static java.time.LocalTime.now;
import static java.util.Objects.hash;

import java.io.Serializable;
import java.security.KeyPair;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class Peer implements Serializable {

  private final int port;
  private final String username;
  private final String address;
  private final Set<Role> roleList = new HashSet<>();
  private long lastHearbeat;

  public Peer(int port, String username, KeyPair keyPair, Role role) {
    this.port = port;
    this.username = username;
    this.address = applySha256(new String(keyPair.getPublic().getEncoded()));
    this.roleList.add(role);
    this.lastHearbeat = now().toNanoOfDay();
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

  public boolean hasRole(Role role) {
    return roleList.contains(role);
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

  public Peer isAlive(LocalTime now) {
    if (now.isAfter(LocalTime.ofNanoOfDay(lastHearbeat).plusSeconds(10))) {
      return this;
    }
    return null;
  }

  public void updateLastHeartbeat() {
    this.lastHearbeat = now().toNanoOfDay();
  }
}
