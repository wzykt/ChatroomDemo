package com.kts.message.heartbeat;

import com.kts.dispacher.Message;

/**
 * 消息 - 心跳请求
 */
public class HeartbeatRequest implements Message {

    /**
     * 类型 - 心跳请求
     */
    public static final String TYPE = "HEARTBEAT_REQUEST";

    @Override
    public String toString() {
        return "HeartbeatRequest{}";
    }

}