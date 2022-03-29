package com.xb.im.client.core.encoder;

import com.xb.im.common.entity.LoginMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
public class LoginMessageEncoder implements Encoder {

  @Override
  public ByteBuf encode(Object o) {
    ByteBuf buffer = Unpooled.buffer();
    LoginMessage loginMessage = (LoginMessage) o;
    buffer.writeInt(loginMessage.getOperate());
    buffer.writeBytes(loginMessage.getName().getBytes());
    buffer.writeBytes(loginMessage.getPassword().getBytes());
    buffer.writeBytes("\r\n".getBytes());
    return buffer;
  }

  @Override
  public boolean support(Object o) {
    return o instanceof LoginMessage;
  }
}
