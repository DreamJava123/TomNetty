package the.flash.server.handler.inbound;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import the.flash.protocol.PacketCodeC;
import the.flash.protocol.request.LoginRequestPacket;
import the.flash.protocol.response.LoginResponsePacket;

public class LoginCheck extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    System.out.println(JSON.toJSONString(msg));
    if (msg instanceof LoginRequestPacket) {
      //交给别的线程去做 不阻塞线程?? 还是当前handle去做? 还是缓存校验? 还是通过channel去做校验
      LoginRequestPacket loginRequestPacket = (LoginRequestPacket) msg;
      if (!loginRequestPacket.getPassword().equalsIgnoreCase("dsada")) {
        ctx.channel().close().addListener(future -> {
          //监听回调方法
          if (future.isSuccess()) {
            System.out.println("close success!!!!");
          }
        });
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSuccess(false);
        loginResponsePacket.setReason("密码不对 请重新输入!!!");
        ByteBuf encode = PacketCodeC.INSTANCE.encode(ctx.channel().alloc(), loginResponsePacket);
        ctx.channel().writeAndFlush(encode);
      } else {
        ctx.fireChannelRead(msg);
        System.out.println(loginRequestPacket.toString());
      }
    }
  }
}
