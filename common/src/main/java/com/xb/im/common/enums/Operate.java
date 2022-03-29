package com.xb.im.common.enums;

import org.msgpack.annotation.Message;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
@Message
public enum Operate {
  LOGIN(0);

  private int code;

  Operate(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
