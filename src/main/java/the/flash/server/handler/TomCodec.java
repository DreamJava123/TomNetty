package the.flash.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * Created by TOM
 * On 2019/9/24 16:31
 */
public class TomCodec extends ByteToMessageDecoder {

  private static final int MAGIC_NUMBER = 0x12345678;

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    System.out.println(MAGIC_NUMBER);
    System.out.println(in.getInt(0));
    if (in.getInt(0) != MAGIC_NUMBER) {
      ctx.channel().close();
      return;
    }
    ctx.fireChannelRead(in);
  }
}
