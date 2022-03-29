package com.xb.im.client.core.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.util.List;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
public class MessageEncoder extends MessageToByteEncoder {

  private List<Encoder> encoders;

  public MessageEncoder(List<Encoder> encoders) {
    this.encoders = encoders;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
    for (Encoder encoder : encoders) {
      if (encoder.support(msg)) {
        ByteBuf buf = encoder.encode(msg);
        out.writeBytes(buf.array());
      }
    }
  }
}
