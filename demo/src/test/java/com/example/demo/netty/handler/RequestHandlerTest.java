package com.example.demo.netty.handler;

import com.example.demo.models.CardModel;
import com.example.demo.models.RequestModel;
import com.example.demo.utils.Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestHandlerTest {

    @AfterEach
    void tearDown() {
    }

    @Test
    void channelRead() {
        EmbeddedChannel channel = new EmbeddedChannel() ;
        channel.pipeline().addLast(new RequestHandler()) ;


        CardModel model = CardModel.builder()
                .cardNo("5133310023055946")
                .amount(90000)
                .vat(9000)
                .build() ;




        ByteBuf buff = Unpooled.buffer() ;
        buff.writeBytes( ("01010044" + model.toString()).getBytes(StandardCharsets.UTF_8)) ;

       channel.writeInbound(buff) ;

        RequestModel requestModel = (RequestModel) channel.readInbound() ;


        assertEquals("0101", requestModel.getMessageType()) ;
        assertEquals(44, requestModel.getTotalSize());

        assertEquals(model.toString(), requestModel.buildMessage());

    }


}