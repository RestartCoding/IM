package com.xb.im.client.core.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class MsgPackEncoder extends MessageToByteEncoder<Object> {

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    MessagePack messagePack = new MessagePack();
    byte[] bytes = messagePack.write(msg);
    out.writeBytes(bytes);
  }
}
