package com.example.demo.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Slf4j
@ChannelHandler.Sharable
@Component
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String readMessage = ((ByteBuf)msg).toString(Charset.defaultCharset()) ;
        log.info("Received message : {}", readMessage) ;
        ctx.write(msg) ;

        ctx.fireChannelRead(msg) ;
     }

     @Override
     public void channelReadComplete(ChannelHandlerContext ctx){

         ctx.flush();
         log.info("channelReadComplete") ;

     }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
        log.error(cause.toString());

        ctx.close() ;
    }
}
