package the.flash.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import the.flash.protocol.PacketCodeC;

/**
 * Created by TOM
 * On 2019/9/26 0:34
 */
public class PackgeDecode extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    //通过解码方法解码 放入out中 下一步使用这个data
    out.add(PacketCodeC.INSTANCE.decode(in));
  }
}
