package com.xb.im.server;

import com.xb.im.common.entity.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import java.util.List;

/**
 * @author xiabiao
 * @date 2022-03-30
 */
public class BusinessServiceDispatcher implements Dispatcher{

  private List<BusinessService> serviceList;

  public BusinessServiceDispatcher(List<BusinessService> serviceList) {
    this.serviceList = serviceList;
  }

  @Override
  public void dispatch(ChannelHandlerContext ctx, NettyMessage message) {
    for (BusinessService businessService : serviceList){
      if (businessService.isSupport(message)){
        businessService.handle(ctx, message);
      }
    }
  }
}
