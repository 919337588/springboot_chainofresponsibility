package com.cx.handler.impl;

import com.cx.handler.GatewayHandler;
import org.springframework.stereotype.Component;


@Component
public class BlacklistHandler extends GatewayHandler {

    public void service() {
        System.out.println("第二关 黑名单拦截.......");
        nextService();
    }
}
