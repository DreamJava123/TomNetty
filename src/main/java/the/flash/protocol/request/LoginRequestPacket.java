package the.flash.protocol.request;

import static the.flash.protocol.command.Command.LOGIN_REQUEST;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import the.flash.protocol.Packet;

@Getter
@Setter
@ToString
public class LoginRequestPacket extends Packet {

  private String userId;

  private String username;

  private String password;

  @Override
  public Byte getCommand() {

    return LOGIN_REQUEST;
  }
}
