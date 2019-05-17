package com.cx.handler.impl;

import com.cx.handler.GatewayHandler;
import org.springframework.stereotype.Component;


@Component
public class ConversationHandler extends GatewayHandler {
    public void service() {
        System.out.println("第三关 用户的会话信息拦截.......");
//        nextService();
    }
}
