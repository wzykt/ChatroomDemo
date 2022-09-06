package com.kts.messageHandler.heartbeat;

import com.kts.codec.Invocation;
import com.kts.dispacher.MessageHandler;
import com.kts.message.heartbeat.HeartbeatRequest;
import com.kts.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 在收到客户端的心跳请求时，回复客户端一条确认消息。
 */
@Component
public class heartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {
    private Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void execute(Channel channel, HeartbeatRequest message) {
        logger.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(HeartbeatRequest.TYPE, response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }
}
