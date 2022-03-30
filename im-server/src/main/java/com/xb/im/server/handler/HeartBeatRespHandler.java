package com.xb.im.server.handler;

import com.xb.im.common.entity.Header;
import com.xb.im.common.entity.NettyMessage;
import com.xb.im.common.enums.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    NettyMessage message = (NettyMessage) msg;
    if (message.getHeader() != null
        && message.getHeader().getType() == MessageType.HEARTBEAT_REQ.getValue()) {
      if (ctx.channel().hasAttr(AttributeKey.valueOf("authenticated"))) {
        System.out.println("received client heart beat message : " + message);
        ctx.writeAndFlush(buildHeartResp());
      } else {
        // 未登录，关闭连接
        ctx.close();
      }
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private NettyMessage buildHeartResp() {
    NettyMessage message = new NettyMessage();
    Header header = new Header();
    header.setType(MessageType.HEARTBEAT_RESP.getValue());
    message.setHeader(header);
    return message;
  }
}
