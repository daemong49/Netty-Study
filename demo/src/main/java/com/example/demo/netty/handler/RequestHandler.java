package com.example.demo.netty.handler;

import com.example.demo.models.RequestModel;
import com.example.demo.utils.Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
@ChannelHandler.Sharable
public class RequestHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buffer ;
    private int totalSize = 0 ;
    private String messageType ;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handler added");
        buffer = ctx.alloc().buffer();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handler removed");
        clearBuffer();
    }

    private void clearBuffer(){
        if(buffer != null){
            buffer.release() ;
            buffer = null ;
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf receivedBuffer = (ByteBuf) msg ;
        log.info("Read {}", receivedBuffer.readableBytes());
        try {

            buffer.writeBytes(receivedBuffer);
            receivedBuffer.release();

            int length = buffer.readableBytes();
            if (length > 8 && totalSize == 0) {
                byte[] bytes = new byte[4];
                buffer.getBytes(0, bytes);

                String header = new String(bytes, StandardCharsets.UTF_8);

                messageType = new String(bytes, StandardCharsets.UTF_8);
                bytes = new byte[4];
                buffer.getBytes(4, bytes);
                totalSize =  ByteBuffer.wrap(bytes).getInt() + 8 ;

                log.info("messageType:{} totalSize:{}", messageType, totalSize);
            }

            if (length == totalSize) {

                byte[] bytes = new byte[buffer.readableBytes() - 8];

                //8byte 이동
                buffer.readLong() ;
                buffer.readBytes(bytes);

                log.info("RECEIVED:{}" , new String(bytes, StandardCharsets.UTF_8));

                RequestModel requestModel = RequestModel.builder()
                        .totalSize(totalSize)
                        .messageType(messageType)
                        .messageBytes(bytes)
                        .build();

                clearBuffer();

                ctx.fireChannelRead(requestModel) ;
            }
        }catch (Exception e){
            log.error(e.toString()) ;
        }

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete");
    }
}
