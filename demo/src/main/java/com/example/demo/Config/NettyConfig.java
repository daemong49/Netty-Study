package com.example.demo.Config;

import com.example.demo.netty.handler.SimpleChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
@Configuration
public class NettyConfig {

    private final NettyProperties properties ;

    @Bean
    public ServerBootstrap serverBootstrap(SimpleChannelInitializer simpleChannelInitializer){

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup(), workerGroup())
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(simpleChannelInitializer) ;

        return bootstrap ;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup(){
        return new NioEventLoopGroup(properties.getBossCount()) ;
    }

    @Bean(destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup(){
        return new NioEventLoopGroup(properties.getWorkerCount()) ;
    }

    @Bean
    public InetSocketAddress tcpSocketAddress(){
        return new InetSocketAddress(properties.getTcpPort()) ;
    }
}
