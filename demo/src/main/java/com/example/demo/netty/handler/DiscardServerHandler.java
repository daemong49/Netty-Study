package com.example.demo.netty.handler;

import com.example.demo.models.CardModel;
import com.example.demo.models.RequestModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@ChannelHandler.Sharable
public class DiscardServerHandler extends SimpleChannelInboundHandler<RequestModel> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RequestModel model) throws Exception {
        log.info("ChannelRead") ;

        CardModel cardModel = CardModel.builder().build();
        cardModel.Generate(model.getMessageBytes()) ;

        String json = new ObjectMapper().writeValueAsString(cardModel) ;

        log.info("json: {}" ,  json) ;
        byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8) ;

        ByteBuf buff = ctx.alloc().buffer() ;
        buff.writeBytes(jsonBytes) ;
        ctx.writeAndFlush(buff) ;

        buff.release() ;


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error(cause.toString());

        ctx.close() ;
    }


}
