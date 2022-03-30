package com.xb.im.server;

import com.xb.im.common.entity.Header;
import com.xb.im.common.entity.NettyMessage;
import com.xb.im.common.enums.MessageType;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public class LoginAuthRespService implements BusinessService {

  @Override
  public boolean isSupport(NettyMessage nettyMessage) {
    return nettyMessage.getHeader() != null
        && nettyMessage.getHeader().getType() == MessageType.LOGIN_REQ.getValue();
  }

  @Override
  public void handle(ChannelHandlerContext ctx, NettyMessage nettyMessage) {
    ctx.channel().writeAndFlush(buildLoginRespMessage((byte) 0));
  }

  private NettyMessage buildLoginRespMessage(byte result) {
    NettyMessage message = new NettyMessage();
    Header header = new Header();
    message.setHeader(header);
    header.setType(MessageType.LOGIN_RESP.getValue());
    message.setBody(result);
    return message;
  }
}
