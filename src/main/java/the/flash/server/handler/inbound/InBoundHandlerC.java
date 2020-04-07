package the.flash.server.handler.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class InBoundHandlerC extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println("InBoundHandlerC: " + msg);
    ByteBuf msg1 = (ByteBuf) msg;
    msg1.writeByte(5);
    msg1.capacity(5656);
    ctx.channel().writeAndFlush(msg1);
    //ctx.channel().writeAndFlush(msg)是执行到最后节点 然后才往前找outboundHandler的 所以肯定会找到后面的三个
    //ctx.writeAndFlush(msg) 这样子只会有
    // InBoundHandlerA: PooledUnsafeDirectByteBuf(ridx: 0, widx: 96, cap: 1024)
    //InBoundHandlerB: PooledUnsafeDirectByteBuf(ridx: 0, widx: 96, cap: 1024)
    //InBoundHandlerC: PooledUnsafeDirectByteBuf(ridx: 0, widx: 96, cap: 1024)
    //因为 ctx.writeAndFlush(msg) 是从当前节点向前找outboundHandler的 后面的三个out逻辑不会走到就返回了
  }
}
