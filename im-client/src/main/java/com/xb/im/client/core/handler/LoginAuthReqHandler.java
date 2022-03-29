package com.xb.im.client.core.handler;

import com.xb.im.common.entity.Header;
import com.xb.im.common.entity.NettyMessage;
import com.xb.im.common.enums.MessageType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    ctx.writeAndFlush(buildLoginReq());
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    NettyMessage message = (NettyMessage) msg;
    // 如果是握手应答消息，需要判断是否认证成功
    if (message.getHeader() != null
        && message.getHeader().getType() == MessageType.LOGIN_RESP.getValue()) {
      ByteBuf byteBuf = Unpooled.copiedBuffer((byte[]) message.getBody());
      byte loginResult = byteBuf.readByte();
      if (loginResult != 0) {
        // 握手失败，关闭连接
        ctx.close();
      } else {
        System.out.println("login is ok : " + message);
        ctx.fireChannelRead(msg);
      }
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private NettyMessage buildLoginReq() {
    NettyMessage message = new NettyMessage();
    Header header = new Header();
    header.setType(MessageType.LOGIN_REQ.getValue());
    message.setHeader(header);
    return message;
  }
}
