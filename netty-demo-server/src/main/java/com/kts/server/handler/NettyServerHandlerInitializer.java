package com.kts.server.handler;

import com.kts.codec.InvocationDecoder;
import com.kts.codec.InvocationEncoder;
import com.kts.dispacher.MessageDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class NettyServerHandlerInitializer extends ChannelInitializer<Channel> {


    /**
     * 心跳超时时间
     */
    private static final Integer READ_TIMEOUT_SECONDS = 60;

    @Autowired
    private MessageDispatcher messageDispatcher;

    @Autowired
    private NettyServerHandler nettyServerHandler;

    @Override
    protected void initChannel(Channel channel) throws Exception {
        // <1> 获得 Channel 对应的 ChannelPipeline
        ChannelPipeline channelPipeline = channel.pipeline();
        // <2> 添加一堆 NettyServerHandler 到 ChannelPipeline 中
        channelPipeline
                // 空闲检测,它在超过指定时间未从对端读取到数据，会抛出 ReadTimeoutException 异常。
                .addLast(new ReadTimeoutHandler(READ_TIMEOUT_SECONDS * 3, TimeUnit.SECONDS))
                // 编码器
                .addLast(new InvocationEncoder())
                // 解码器
                .addLast(new InvocationDecoder())
                // 消息分发器
                .addLast(messageDispatcher)
                // 服务端处理器
                .addLast(nettyServerHandler)
        ;
    }
}
