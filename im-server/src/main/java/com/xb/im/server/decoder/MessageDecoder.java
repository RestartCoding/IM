package com.xb.im.server.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
public class MessageDecoder extends ByteToMessageDecoder {

  private List<Decoder> decoders;

  public MessageDecoder(List<Decoder> decoders) {
    this.decoders = decoders;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    for (Decoder decoder : decoders) {
      if (decoder.support(in)) {
        out.add(decoder.decode(in));
        break;
      }
    }
  }
}
