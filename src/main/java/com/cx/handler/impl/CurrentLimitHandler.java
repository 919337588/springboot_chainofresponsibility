package com.cx.handler.impl;

import com.cx.handler.GatewayHandler;
import org.springframework.stereotype.Component;


@Component
public class CurrentLimitHandler extends GatewayHandler {


    public void service() {
        System.out.println("第一关 API接口限流操作.....");
        nextService();// 指向下一关黑名单
    }
}
