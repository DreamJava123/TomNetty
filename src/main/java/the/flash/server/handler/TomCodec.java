package the.flash.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by TOM
 * On 2019/9/24 16:31
 */
public class TomCodec extends ByteToMessageDecoder {

  private AtomicInteger atomicInteger = new AtomicInteger(0);

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    System.out.println("----------");
    System.out.println(in.readableBytes());
    System.out.println(in.readInt());
    out.add("ddd" + atomicInteger.addAndGet(1));
    //ctx.fireChannelRead("dsadas");
  }
}
