package com.cx.controller;

import com.cx.handler.GatewayHandler;
import com.cx.service.GatewayHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HandlerController {
    @Autowired
    private GatewayHandlerService gatewayHandlerService;

    @RequestMapping("/client")
    public String client() {
//        GatewayHandler gatewayHandler = FactoryHandler.getGatewayHandler();
//        gatewayHandler.service();
        GatewayHandler firstGatewayHandler = gatewayHandlerService.getFirstGatewayHandler();
        firstGatewayHandler.service();
        return "success";
    }
}

