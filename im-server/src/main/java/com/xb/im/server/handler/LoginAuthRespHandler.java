package com.xb.im.server.handler;

import com.xb.im.common.entity.Header;
import com.xb.im.common.entity.NettyMessage;
import com.xb.im.common.enums.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import java.net.InetSocketAddress;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

  private String[] whiteList = {"127.0.0.1"};

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    NettyMessage message = (NettyMessage) msg;

    if (message.getHeader() != null
        && message.getHeader().getType() == MessageType.LOGIN_REQ.getValue()) {
      NettyMessage loginResp = null;
      InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
      String ip = socketAddress.getAddress().getHostAddress();
      boolean isOk = false;
      for (String wip : whiteList) {
        if (wip.equals(ip)) {
          isOk = true;
          break;
        }
      }

      loginResp = isOk ? buildResponse((byte) 0) : buildResponse((byte) -1);
      if (isOk) {
        // 认证成功
        ctx.channel().attr(AttributeKey.valueOf("authenticated"));
        // 登录成功，移除
        ctx.pipeline().remove(LoginAuthRespHandler.class);
      }
      // 返回登录成功消息
      ctx.writeAndFlush(loginResp);
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private NettyMessage buildResponse(byte result) {
    NettyMessage message = new NettyMessage();
    Header header = new Header();
    header.setType(MessageType.LOGIN_RESP.getValue());
    message.setHeader(header);
    message.setBody(result);
    return message;
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    // 关闭channel
    ctx.close();
    ctx.fireExceptionCaught(cause);
  }
}
