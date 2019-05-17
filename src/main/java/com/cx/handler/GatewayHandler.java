package com.cx.handler;


public abstract class GatewayHandler {
    /**
     * 执行下一个handler
     */
    protected GatewayHandler nextGatewayHandler;

    /**
     * 实现的handler 处理方案 强制必须实现
     */
    public abstract void service();


    public void setNextGatewayHandler(GatewayHandler nextGatewayHandler) {
        this.nextGatewayHandler = nextGatewayHandler;
    }

    /**
     *
     */
    protected void nextService() {
        if (nextGatewayHandler != null)
            nextGatewayHandler.service();// 指向下一关黑名单
    }
}
