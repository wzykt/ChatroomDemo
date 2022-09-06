package com.kts.dispacher;

import com.alibaba.fastjson.JSON;
import com.kts.codec.Invocation;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 将 Invocation 分发到其对应的 MessageHandler 中，进行业务逻辑的执行。
 *
 * 在类上添加 @ChannelHandler.Sharable 注解，标记这个 ChannelHandler 可以被多个 Channel 使用。
 *
 * SimpleChannelInboundHandler 是 Netty 定义的消息处理 ChannelHandler 抽象类，处理消息的类型是 <I> 泛型时。
 */
@ChannelHandler.Sharable
public class MessageDispatcher extends SimpleChannelInboundHandler<Invocation> {

    @Autowired
    private MessageHandlerContainer messageHandlerContainer;


    private final ExecutorService executor = Executors.newFixedThreadPool(200);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Invocation invocation) {
        // 获得 type 对应的 MessageHandler 处理器
        MessageHandler messageHandler = messageHandlerContainer.getMessageHandler(invocation.getType());
        // 获得  MessageHandler 处理器的消息类
        Class<? extends Message> messageClass = MessageHandlerContainer.getMessageClass(messageHandler);
        // 解析消息
        Message message = JSON.parseObject(invocation.getMessage(), messageClass);
        // 执行逻辑
        executor.submit(new Runnable() {

            @Override
            public void run() {
                // noinspection unchecked
                messageHandler.execute(ctx.channel(), message);
            }

        });
    }

}
