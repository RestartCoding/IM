package com.xb.im.client.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class ExceptionHandler extends ChannelOutboundHandlerAdapter {

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    System.out.println(cause.getMessage());
  }
}
