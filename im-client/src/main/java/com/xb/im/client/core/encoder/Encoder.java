package com.xb.im.client.core.encoder;

import io.netty.buffer.ByteBuf;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
public interface Encoder {

  ByteBuf encode(Object o);

  boolean support(Object o);
}
