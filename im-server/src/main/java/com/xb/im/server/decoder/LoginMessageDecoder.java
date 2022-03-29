package com.xb.im.server.decoder;

import com.xb.im.common.entity.LoginMessage;
import com.xb.im.common.enums.Operate;
import io.netty.buffer.ByteBuf;
import java.nio.charset.StandardCharsets;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
public class LoginMessageDecoder implements Decoder {

  @Override
  public Object decode(ByteBuf buf) {
    buf.readerIndex(4);
    String username = buf.readBytes(new byte[32]).toString(StandardCharsets.UTF_8);
    String password = buf.readBytes(new byte[32]).toString(StandardCharsets.UTF_8);
    LoginMessage loginMessage = new LoginMessage();
    loginMessage.setOperate(Operate.LOGIN.getCode());
    loginMessage.setName(username);
    loginMessage.setPassword(password);
    return loginMessage;
  }

  @Override
  public boolean support(ByteBuf buf) {
    int operateCode = buf.getInt(0);
    return operateCode == Operate.LOGIN.getCode();
  }
}
