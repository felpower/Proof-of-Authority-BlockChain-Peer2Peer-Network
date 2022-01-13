package helper;

import static java.net.InetAddress.getLocalHost;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPHelper {

  public static InetAddress getGroup() throws UnknownHostException {
    return InetAddress.getByName("230.0.0.0");
  }

  public static InetAddress getLocalGroup() throws UnknownHostException {
    return getLocalHost();
  }

  public static int getPort() {
    return 4999;
  }

}
