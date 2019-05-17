package com.cx.mapper.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class GatewayHandlerEntity implements Serializable, Cloneable {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * handler名称
     */
    private String handlerName;
    /**
     * handler主键id
     */
    private String handlerId;
    /**
     * 下一个handler
     */
    private String nextHandlerId;


}
