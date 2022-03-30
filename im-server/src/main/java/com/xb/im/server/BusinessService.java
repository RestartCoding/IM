package com.xb.im.server;

import com.xb.im.common.entity.NettyMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public interface BusinessService {

  boolean isSupport(NettyMessage nettyMessage);

  void handle(ChannelHandlerContext ctx, NettyMessage message);
}
