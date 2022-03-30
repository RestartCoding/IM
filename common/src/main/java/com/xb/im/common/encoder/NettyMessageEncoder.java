package com.xb.im.common.encoder;

import com.xb.im.common.entity.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.io.IOException;
import java.util.List;
import org.msgpack.MessagePack;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {

  private MessagePack messagePack;

  public NettyMessageEncoder() {
    this.messagePack = new MessagePack();
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out)
      throws Exception {
    ByteBuf buf = Unpooled.buffer();
    buf.writeInt(msg.getHeader().getCrcCode());
    buf.writeInt(msg.getHeader().getLength());
    buf.writeLong(msg.getHeader().getSessionId());
    buf.writeByte(msg.getHeader().getType());
    buf.writeByte(msg.getHeader().getPriority());
    if (msg.getHeader().getAttachment() != null) {
      buf.writeInt(msg.getHeader().getAttachment().size());
      msg.getHeader()
          .getAttachment()
          .forEach(
              (k, v) -> {
                byte[] keyArray = k.getBytes();
                buf.writeInt(keyArray.length);
                buf.writeBytes(keyArray);
                try {
                  byte[] valueBytes = messagePack.write(v);
                  buf.writeInt(valueBytes.length);
                  buf.writeBytes(valueBytes);
                } catch (IOException e) {
                  throw new RuntimeException(e);
                }
              });
    } else {
      buf.writeInt(0);
    }
    if (msg.getBody() != null) {
      buf.writeBytes(messagePack.write(msg.getBody()));
    }
    buf.setInt(4, buf.readableBytes() - 8);
    out.add(buf);
  }
}
