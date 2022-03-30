package com.xb.im.client.core.handler;

import com.xb.im.common.entity.Header;
import com.xb.im.common.entity.NettyMessage;
import com.xb.im.common.enums.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public class HeartbeatReqHandler extends ChannelInboundHandlerAdapter {

  private volatile ScheduledFuture<?> scheduledFuture;

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    NettyMessage message = (NettyMessage) msg;
    if (message.getHeader() != null
        && message.getHeader().getType() == MessageType.LOGIN_RESP.getValue()) {
      scheduledFuture =
          ctx.executor().scheduleAtFixedRate(new HeartbeatTask(ctx), 0, 5, TimeUnit.SECONDS);
    } else if (message.getHeader() != null
        && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.getValue()) {
      System.out.println("client received server heart beat message : " + message);
    } else {
      ctx.fireChannelRead(msg);
    }
  }

  private static class HeartbeatTask implements Runnable {

    private ChannelHandlerContext ctx;

    public HeartbeatTask(ChannelHandlerContext ctx) {
      this.ctx = ctx;
    }

    @Override
    public void run() {
      NettyMessage heartbeat = buildHeartbeat();
      System.out.println("client send heart beat message to server : " + heartbeat);
      ctx.writeAndFlush(heartbeat);
    }

    private NettyMessage buildHeartbeat() {
      NettyMessage message = new NettyMessage();
      Header header = new Header();
      message.setHeader(header);
      header.setType(MessageType.HEARTBEAT_REQ.getValue());
      return message;
    }
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    if (scheduledFuture != null) {
      scheduledFuture.cancel(true);
      scheduledFuture = null;
    }
    ctx.fireExceptionCaught(cause);
  }
}
