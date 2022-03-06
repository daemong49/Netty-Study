package com.example.demo.netty.codec;

import com.example.demo.models.RequestModel;
import com.example.demo.utils.Util;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Slf4j
public class ToRequestModelDecoder extends ReplayingDecoder<Void> {

    private ByteBuf buffer ;
    private int totalSize = 0 ;
    private String messageType ;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handler added");
        buffer = ctx.alloc().buffer();
    }

    @Override
    protected void handlerRemoved0(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved0(ctx);

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
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        log.info("Read {}", byteBuf.readableBytes());

        try {

            buffer.writeBytes(byteBuf);
            byteBuf.release();

            int length = buffer.readableBytes();
            if (length > 8 && totalSize == 0) {
                byte[] bytes = new byte[8];
                buffer.getBytes(0, bytes);

                String header = new String(bytes, StandardCharsets.UTF_8);
                Map.Entry<String, Integer> values = Util.parsingMessageHeader(header);

                messageType = values.getKey();
                totalSize = values.getValue();

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

                list.add(requestModel) ;

                log.info("added");

            }


        }catch (Exception e){
            e.printStackTrace(); ;
        }
    }
}
