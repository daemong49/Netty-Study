package com.example.demo.netty.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Channel Initializer
 * @author sky
 */
@Component
@RequiredArgsConstructor
public class SimpleChannelInitializer extends ChannelInitializer<SocketChannel> {

    private final DiscardServerHandler discardServerHandler ;
    private final RequestHandler requesthandler ;

    @Override
    protected void initChannel(SocketChannel socketChannel)  {
        ChannelPipeline pipeLine = socketChannel.pipeline() ;
        pipeLine.addLast(new LoggingHandler(LogLevel.DEBUG));
        pipeLine.addLast(requesthandler) ;
        pipeLine.addLast(discardServerHandler) ;
    }
}
