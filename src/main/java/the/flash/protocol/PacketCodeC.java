package the.flash.protocol;

import static the.flash.protocol.command.Command.LOGIN_REQUEST;
import static the.flash.protocol.command.Command.LOGIN_RESPONSE;
import static the.flash.protocol.command.Command.MESSAGE_REQUEST;
import static the.flash.protocol.command.Command.MESSAGE_RESPONSE;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import java.util.HashMap;
import java.util.Map;
import the.flash.protocol.request.LoginRequestPacket;
import the.flash.protocol.request.MessageRequestPacket;
import the.flash.protocol.response.LoginResponsePacket;
import the.flash.protocol.response.MessageResponsePacket;
import the.flash.serialize.Serializer;
import the.flash.serialize.impl.JSONSerializer;

public class PacketCodeC {

  private static final int MAGIC_NUMBER = 0x12345678;
  public static final PacketCodeC INSTANCE = new PacketCodeC();

  private final Map<Byte, Class<? extends Packet>> packetTypeMap;
  private final Map<Byte, Serializer> serializerMap;


  private PacketCodeC() {
    packetTypeMap = new HashMap<>();
    packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
    packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
    packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
    packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);

    serializerMap = new HashMap<>();
    Serializer serializer = new JSONSerializer();
    serializerMap.put(serializer.getSerializerAlogrithm(), serializer);
  }


  public ByteBuf encode(ByteBufAllocator byteBufAllocator, Packet packet) {
    // 1. 创建 ByteBuf 对象
    ByteBuf byteBuf = byteBufAllocator.ioBuffer();
    // 2. 序列化 java 对象
    byte[] bytes = Serializer.DEFAULT.serialize(packet);

    // 3. 实际编码过程
    byteBuf.writeInt(MAGIC_NUMBER);//4
    byteBuf.writeByte(packet.getVersion());//1
    byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlogrithm());//1
    byteBuf.writeByte(packet.getCommand());//1
    byteBuf.writeInt(bytes.length);//4
    byteBuf.writeBytes(bytes);

    return byteBuf;
  }


  public Packet decode(ByteBuf byteBuf) {
    // 跳过 magic number
    byteBuf.skipBytes(4);

    // 跳过版本号
    byteBuf.skipBytes(1);

    // 序列化算法
    byte serializeAlgorithm = byteBuf.readByte();

    // 指令
    byte command = byteBuf.readByte();

    // 数据包长度
    int length = byteBuf.readInt();

    byte[] bytes = new byte[length];
    byteBuf.readBytes(bytes);

    Class<? extends Packet> requestType = getRequestType(command);
    Serializer serializer = getSerializer(serializeAlgorithm);

    if (requestType != null && serializer != null) {
      return serializer.deserialize(requestType, bytes);
    }

    return null;
  }

  private Serializer getSerializer(byte serializeAlgorithm) {

    return serializerMap.get(serializeAlgorithm);
  }

  private Class<? extends Packet> getRequestType(byte command) {

    return packetTypeMap.get(command);
  }
}
