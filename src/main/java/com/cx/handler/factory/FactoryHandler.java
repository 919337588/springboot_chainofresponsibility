package com.cx.handler.factory;

import com.cx.handler.GatewayHandler;
import com.cx.handler.impl.BlacklistHandler;
import com.cx.handler.impl.ConversationHandler;
import com.cx.handler.impl.CurrentLimitHandler;


public class FactoryHandler {

    public static GatewayHandler getGatewayHandler() {
        // 1.使用工厂模式封装Handler责任链
        GatewayHandler gatewayHandler1 = new CurrentLimitHandler();
        GatewayHandler gatewayHandler2 = new BlacklistHandler();
        gatewayHandler1.setNextGatewayHandler(gatewayHandler2);
        GatewayHandler gatewayHandler3 = new ConversationHandler();
        gatewayHandler2.setNextGatewayHandler(gatewayHandler3);
        return gatewayHandler1;
    }

}
