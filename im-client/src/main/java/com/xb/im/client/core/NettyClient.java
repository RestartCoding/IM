package com.xb.im.client.core;

import com.xb.im.client.core.handler.HeartbeatReqHandler;
import com.xb.im.client.core.handler.LoginAuthReqHandler;
import com.xb.im.common.decoder.NettyMessageDecoder;
import com.xb.im.common.encoder.NettyMessageEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public class NettyClient {

  private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

  private EventLoopGroup group = new NioEventLoopGroup();

  public void connect(String host, int port) throws InterruptedException {
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap
          .group(group)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline pipeline = ch.pipeline();
                  pipeline.addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                  pipeline.addLast(new NettyMessageEncoder());
                  pipeline.addLast(new ReadTimeoutHandler(50));
                  pipeline.addLast(new LoginAuthReqHandler());
                  pipeline.addLast(new HeartbeatReqHandler());
                }
              });
      // 发起异步连接
      ChannelFuture future = bootstrap.connect(host, port).sync();
      future.channel().closeFuture().sync();
    } finally {
      executor.execute(
          () -> {
            try {
              TimeUnit.SECONDS.sleep(5);
              try {
                connect(host, port);
              } catch (Exception e) {
                e.printStackTrace();
              }
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          });
    }
  }

  public static void main(String[] args) throws InterruptedException {
    new NettyClient().connect("127.0.0.1", 8080);
  }
}
