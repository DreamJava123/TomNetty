package the.flash.server.handler.inbound;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import the.flash.protocol.request.LoginRequestPacket;

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
      } else {
        ctx.fireChannelRead(msg);
        System.out.println(loginRequestPacket.toString());
      }
    }
  }
}
