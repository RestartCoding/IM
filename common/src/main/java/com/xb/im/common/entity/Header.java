package com.xb.im.common.entity;

import java.util.Map;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class Header {

  private int crcCode = 0XABEF0101;

  private int length;

  private long sessionId;

  private byte type;

  private byte priority;

  private Map<String, Object> attachment;

  public int getCrcCode() {
    return crcCode;
  }

  public void setCrcCode(int crcCode) {
    this.crcCode = crcCode;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public long getSessionId() {
    return sessionId;
  }

  public void setSessionId(long sessionId) {
    this.sessionId = sessionId;
  }

  public byte getType() {
    return type;
  }

  public void setType(byte type) {
    this.type = type;
  }

  public byte getPriority() {
    return priority;
  }

  public void setPriority(byte priority) {
    this.priority = priority;
  }

  public Map<String, Object> getAttachment() {
    return attachment;
  }

  public void setAttachment(Map<String, Object> attachment) {
    this.attachment = attachment;
  }
}
