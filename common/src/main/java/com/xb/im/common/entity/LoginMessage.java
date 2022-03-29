package com.xb.im.common.entity;

import org.msgpack.annotation.Message;

/**
 * @author xiabiao
 * @date 2022-03-28
 */
@Message
public class LoginMessage {

  private Integer operate;

  private String name;

  private String password;

  public Integer getOperate() {
    return operate;
  }

  public void setOperate(Integer operate) {
    this.operate = operate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "LoginMessage{" +
        "operate=" + operate +
        ", name='" + name + '\'' +
        ", password='" + password + '\'' +
        '}';
  }
}
