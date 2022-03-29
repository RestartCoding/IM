package com.xb.im.server.decoder;

import com.xb.im.common.entity.LoginMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.msgpack.MessagePack;
import org.msgpack.type.Value;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class MsgPackDecoder extends MessageToMessageDecoder<ByteBuf> {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
    byte[] dst = new byte[msg.readableBytes()];
    msg.readBytes(dst);
    MessagePack messagePack = new MessagePack();
    LoginMessage loginMessage = messagePack.read(dst, LoginMessage.class);
    out.add(loginMessage);
  }
}
