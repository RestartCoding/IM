package com.xb.im.server.handler;

import com.xb.im.common.entity.Header;
import com.xb.im.common.entity.NettyMessage;
import com.xb.im.common.enums.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {

  private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<>();

  private String[] whiteList = {"127.0.0.1"};

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    NettyMessage message = (NettyMessage) msg;
    if (message.getHeader() != null
        && message.getHeader().getType() == MessageType.LOGIN_REQ.getValue()) {
      String nodeKey = ctx.channel().remoteAddress().toString();
      NettyMessage loginResp = null;
      if (nodeCheck.containsKey(nodeKey)) {
        // 重复登录 拒绝

      } else {
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
          nodeCheck.put(nodeKey, true);
        }
        ctx.writeAndFlush(loginResp);
      }
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
    // 清理缓存
    nodeCheck.remove(ctx.channel().remoteAddress().toString());
    ctx.close();
    ctx.fireExceptionCaught(cause);
  }
}
