package the.flash.server.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by TOM
 * On 2019/9/24 16:31
 */
public class TomCodec extends LengthFieldBasedFrameDecoder {

  private static final int MAGIC_NUMBER = 0x12345678;
  //数据包协议偏移量
  private static final int lengthFieldOffset = 7;
  //协议请求data 字节长度 我这边定义的还是一个int值长度
  private static final int lengthFieldLength = 4;

  public TomCodec() {
    super(Integer.MAX_VALUE, lengthFieldOffset, lengthFieldLength);
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    if (in.getInt(in.readerIndex()) != MAGIC_NUMBER) {
      //校验请求头魔数 不是此协议 关闭连接
      ctx.channel().close();
      return null;
    }
    return super.decode(ctx, in);
  }
}
