package com.kts.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MessageToByteEncoder 是 Netty 定义的编码 ChannelHandler 抽象类，将泛型 <I> 消息转换成字节数组。
 */
public class InvocationEncoder extends MessageToByteEncoder<Invocation> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Invocation invocation, ByteBuf byteBuf) throws Exception {
        //将 Invocation 转换成 字节数组。
        byte[] content = JSON.toJSONBytes(invocation);
        //将字节数组的长度，写入到 TCP Socket 当中。后续3.4 InvocationDecoder可以根据该长度，解析到消息，解决粘包和拆包的问题。
        //MessageToByteEncoder 会最终将 ByteBuf out 写到 TCP Socket 中。
        byteBuf.writeInt(content.length);
        //将字节数组，写入到 TCP Socket 当中。
        byteBuf.writeBytes(content);
        logger.info("[encode][连接({}) 编码了一条消息({})]", channelHandlerContext.channel().id(), invocation.toString());
    }
}
