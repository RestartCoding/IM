package com.xb.im.client.core;

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
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.msgpack.MessagePack;

/**
 * Sends one message when a connection is open and echoes back any received data to the server.
 * Simply put, the echo client initiates the ping-pong traffic between the echo client and server by
 * sending the first message to the server.
 */
public final class EchoClient {

  public static void main(String[] args) throws Exception {
    // Configure the client.
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
          .channel(NioSocketChannel.class)
          .option(ChannelOption.TCP_NODELAY, true)
          .handler(
              new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                  ChannelPipeline p = ch.pipeline();
                  p.addLast(new LoggingHandler(LogLevel.INFO));
                  p.addLast(new NettyMessageDecoder(1024 * 1024, 4, 4));
                  p.addLast(new NettyMessageEncoder(new MessagePack()));
                  p.addLast(new LoginAuthReqHandler());
                }
              });

      // Start the client.
      ChannelFuture f = b.connect("localhost", 8080).sync();

      // Wait until the connection is closed.
      f.channel().closeFuture().sync();
    } finally {
      // Shut down the event loop to terminate all threads.
      group.shutdownGracefully();
    }
  }
}
