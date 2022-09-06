package com.kts.codec;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.CorruptedFrameException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * ByteToMessageDecoder 是 Netty 定义的解码 ChannelHandler 抽象类，在 TCP Socket 读取到新数据时，触发进行解码。
 */
public class InvocationDecoder extends ByteToMessageDecoder {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf in, List<Object> out) throws Exception {
        //标记当前读取位置
        in.markReaderIndex();
        //判断是否能够读取 length 长度
        if (in.readableBytes() <= 4) {
            return;
        }
        //读取长度
        int length = in.readInt();
        if (length < 0) {
            throw new CorruptedFrameException("negative length: " + length);
        }
        //如果 message 不够可读，则退回到原读取位置
        if (in.readableBytes() < length) {
            in.resetReaderIndex();
            return;
        }
        //上面的步骤都是在读取长度
        //读取内容，反序列化
        byte[] content = new byte[length];
        in.readBytes(content);
        Invocation invocation = JSON.parseObject(content, Invocation.class);
        //最终，添加 List<Object> out 中，交给后续的 ChannelHandler 进行处理。
        out.add(invocation);
        logger.info("[decode][连接({}) 解析到一条消息({})]", channelHandlerContext.channel().id(), invocation.toString());
    }
}
