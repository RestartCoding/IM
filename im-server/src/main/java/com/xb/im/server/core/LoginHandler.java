package com.xb.im.server.core;

import com.xb.im.common.entity.LoginMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
public class LoginHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    LoginMessage loginMessage = (LoginMessage) msg;
    System.out.println(loginMessage);
  }
}
