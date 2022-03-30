package com.xb.im.server;

import com.xb.im.common.entity.NettyMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public interface Dispatcher {
  void dispatch(ChannelHandlerContext ctx, NettyMessage message);
}
