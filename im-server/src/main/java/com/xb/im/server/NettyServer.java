package com.xb.im.server;

import com.xb.im.common.decoder.NettyMessageDecoder;
import com.xb.im.common.encoder.NettyMessageEncoder;
import com.xb.im.server.handler.HeartBeatRespHandler;
import com.xb.im.server.handler.LoginAuthRespHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public class NettyServer {

  public void bind(String host, int port) throws InterruptedException {
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup();
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    serverBootstrap.group(boss, worker)
        .channel(NioServerSocketChannel.class)
        .option(ChannelOption.SO_BACKLOG, 100)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
            pipeline.addLast(new NettyMessageEncoder());
            pipeline.addLast(new ReadTimeoutHandler(50));
            pipeline.addLast(new LoginAuthRespHandler());
            pipeline.addLast(new HeartBeatRespHandler());
          }
        });
    serverBootstrap.bind(host, port).sync();
  }

  public static void main(String[] args) throws InterruptedException {
    new NettyServer().bind("127.0.0.1", 8080);
  }
}
