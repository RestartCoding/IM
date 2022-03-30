package com.xb.im.common.enums;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public enum MessageType {
  /** 登录请求 */
  LOGIN_REQ((byte) 0),

  /** 登录响应 */
  LOGIN_RESP((byte) 1),

  HEARTBEAT_REQ((byte) 2),

  HEARTBEAT_RESP((byte) 3);

  private byte value;

  MessageType(byte value) {
    this.value = value;
  }

  public byte getValue() {
    return value;
  }
}
