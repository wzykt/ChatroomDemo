package com.kts.codec;

import com.alibaba.fastjson.JSON;
import com.kts.dispacher.Message;

/**
 * 通信协议的消息体。
 */
public class Invocation {

    /**
     * 类型，用于匹配对应的消息处理器。如果类比 HTTP 协议，type 属性相当于请求地址。
     */
    private String type;
    /**
     * 消息，JSON 格式，后期修改为Protobuf
     */
    private String message;

    // 空构造方法
    public Invocation() {
    }

    public Invocation(String type, String message) {
        this.type = type;
        this.message = message;
    }

    public Invocation(String type, Message message) {
        this.type = type;
        this.message = JSON.toJSONString(message);
    }

    // ... 省略 setter、getter、toString 方法


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}