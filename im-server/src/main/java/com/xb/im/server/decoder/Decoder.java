package com.xb.im.server.decoder;

import io.netty.buffer.ByteBuf;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
public interface Decoder {

  /**
   * 解码
   *
   * @param buf buf
   * @return obj
   */
  Object decode(ByteBuf buf);

  /**
   * 是否支持
   *
   * @param buf buf
   * @return whether support
   */
  boolean support(ByteBuf buf);
}
