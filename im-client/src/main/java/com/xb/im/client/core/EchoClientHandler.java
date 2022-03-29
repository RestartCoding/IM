package com.xb.im.client.core;

import com.xb.im.common.entity.LoginMessage;
import com.xb.im.common.enums.Operate;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;

/**
 * Handler implementation for the echo client. It initiates the ping-pong traffic between the echo
 * client and server by sending the first message to the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

  /** Creates a client-side handler. */
  public EchoClientHandler() {}

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    LoginMessage loginMessage = new LoginMessage();
    loginMessage.setOperate(Operate.LOGIN.getCode());
    loginMessage.setName("1234567890123456");
    loginMessage.setPassword("1234567890123456");
    ctx.writeAndFlush(loginMessage);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf buf = (ByteBuf) msg;
    System.out.println("received msg: " + buf.toString(StandardCharsets.UTF_8));
    ctx.write(msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    // Close the connection when an exception is raised.
    cause.printStackTrace();
    ctx.close();
  }
}
