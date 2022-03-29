package com.xb.im.common.entity;

/**
 * @author xiabiao
 * @date 2022-03-29
 */
public class NettyMessage {

  private Header header;

  private Object body;

  public Header getHeader() {
    return header;
  }

  public void setHeader(Header header) {
    this.header = header;
  }

  public Object getBody() {
    return body;
  }

  public void setBody(Object body) {
    this.body = body;
  }
}
