package com.xb.im.http.server;

import com.xb.im.http.server.handler.HttpFileServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public class HttpFileServer {
  public static final String DEFAULT_URL = "/src/com/";

  public void run(String url, int port) throws InterruptedException {
    EventLoopGroup boss = new NioEventLoopGroup();
    EventLoopGroup worker = new NioEventLoopGroup();
    try {
      ServerBootstrap serverBootstrap = new ServerBootstrap();
      serverBootstrap
          .group(boss, worker)
          .channel(NioServerSocketChannel.class)
          .handler(new LoggingHandler(LogLevel.INFO))
          .childHandler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline pipeline = ch.pipeline();
                  pipeline.addLast(new HttpRequestDecoder());
                  pipeline.addLast(new HttpObjectAggregator(65536));
                  pipeline.addLast(new HttpResponseEncoder());
                  pipeline.addLast(new ChunkedWriteHandler());
                  pipeline.addLast(new HttpFileServerHandler());
                }
              });
      ChannelFuture future = serverBootstrap.bind("127.0.0.1", port).sync();
      future.channel().closeFuture().sync();
    } finally {
      boss.shutdownGracefully();
      worker.shutdownGracefully();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    int port = 8080;
    String url = DEFAULT_URL;
    new HttpFileServer().run(url, port);
  }
}
