package com.xb.im.common.decoder;

import com.xb.im.common.entity.Header;
import com.xb.im.common.entity.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import java.util.HashMap;
import java.util.Map;
import org.msgpack.MessagePack;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

  private MessagePack messagePack;

  public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
    super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    messagePack = new MessagePack();
  }

  @Override
  protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
    ByteBuf frame = (ByteBuf) super.decode(ctx, in);
    if (frame == null) {
      return null;
    }
    NettyMessage nettyMessage = new NettyMessage();
    Header header = new Header();
    in.readerIndex(0);
    header.setCrcCode(in.readInt());
    header.setLength(in.readInt());
    header.setSessionId(in.readLong());
    header.setType(in.readByte());
    header.setPriority(in.readByte());

    int size = in.readInt();
    Map<String, Object> map = new HashMap<>(size);
    header.setAttachment(map);

    for (int i = 0; i < size; i++) {
      int keySize = in.readInt();
      byte[] keyBytes = new byte[keySize];
      in.readBytes(keyBytes);
      String key = new String(keyBytes);
      int valueSize = in.readInt();
      byte[] valueBytes = new byte[valueSize];
      Object value = messagePack.read(valueBytes);
      map.put(key, value);
    }
    if (in.readableBytes() > 0) {
      byte[] body = new byte[in.readableBytes()];
      in.readBytes(body);
      nettyMessage.setBody(body);
    }
    nettyMessage.setHeader(header);
    return nettyMessage;
  }
}
