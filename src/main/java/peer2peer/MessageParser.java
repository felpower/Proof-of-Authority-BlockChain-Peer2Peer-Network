package peer2peer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.DatagramPacket;

public class MessageParser {

  private static ObjectMapper objectMapper = new ObjectMapper();

  public static Message parseMessage(DatagramPacket packet) throws IOException {
    return objectMapper.readValue(packet.getData(), Message.class);
  }
}
