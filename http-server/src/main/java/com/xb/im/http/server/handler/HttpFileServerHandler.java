package com.xb.im.http.server.handler;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.stream.ChunkedFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
    if (!msg.decoderResult().isSuccess()) {
      // 解析错误
      return;
    }
    if (msg.method() != HttpMethod.GET) {
      // 发送错误
      return;
    }
    String uri = msg.uri();
    String path = sanitizeUri(uri);

    if (path == null) {
      // 发送错误
      return;
    }
    File file = new File(path);
    if (file.isHidden() || !file.exists()) {
      // 发送错误
      return;
    }
    if (!file.isFile()) {
      // 发送错误
      return;
    }

    RandomAccessFile randomAccessFile = null;
    try {
      randomAccessFile = new RandomAccessFile(file, "r");
    } catch (FileNotFoundException e) {
      // 发送错误
      return;
    }
    long fileLength = randomAccessFile.length();
    DefaultHttpResponse response =
        new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    response.headers().add(HttpHeaderNames.CONTENT_LENGTH, fileLength);
    response.headers().add(HttpHeaderNames.CONTENT_DISPOSITION, HttpHeaderValues.ATTACHMENT);
    ctx.write(response);
    ChannelFuture sendFileFuture =
        ctx.writeAndFlush(
            new ChunkedFile(randomAccessFile, 0, fileLength, 8192), ctx.newProgressivePromise());
    sendFileFuture.addListener(
        new ChannelProgressiveFutureListener() {
          @Override
          public void operationProgressed(
              ChannelProgressiveFuture channelProgressiveFuture, long l, long l1) throws Exception {
            System.out.println("开始发送文件");
          }

          @Override
          public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture)
              throws Exception {
            System.out.println("文件发送成功");
          }
        });
  }

  private String sanitizeUri(String uri) {
    try {
      uri = URLDecoder.decode(uri, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      try {
        uri = URLDecoder.decode(uri, "ISO-8859-1");
      } catch (UnsupportedEncodingException ex) {
        throw new Error();
      }
    }
    uri = uri.replace("/", File.separator);
    return System.getProperty("user.dir") + File.separator + "http-server" + File.separator + uri;
  }
}
