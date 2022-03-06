package com.example.demo.models;

import io.netty.buffer.ByteBuf;
import lombok.Builder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Getter
@Builder
public class RequestModel {

    private final String messageType ;
    private final Integer totalSize ;
    private final byte[] messageBytes ;



    public CardModel buildMessage(){
        CardModel model = null ;
        String message = "" ;
        if(messageBytes != null){
            message = new String(messageBytes, StandardCharsets.UTF_8) ;

            model = CardModel.builder().build() ;
            model.Generate(messageBytes);
        }



        return model ;
    }
}
