package com.example.demo;

import com.example.demo.Config.NettyProperties;

import com.example.demo.models.CardModel;
import com.example.demo.models.RequestModel;
import com.example.demo.netty.ApprovalServer;
import com.example.demo.netty.codec.RequestDecoder;
import com.example.demo.netty.codec.ToRequestModelDecoder;
import com.example.demo.netty.handler.DiscardServerHandler;
import com.example.demo.netty.handler.RequestHandler;
import com.example.demo.utils.Util;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class DemoApplication {

	final NettyProperties properties ;
	final Environment environment ;
	final ApprovalServer server ;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		log.info("hi");
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			log.info("Let's inspect the beans provided by Spring Boot:");
			log.info(properties.toString()) ;
			log.info(environment.getProperty("option")) ;

			this.bufferTest();

			// server.start();
		};


	}

	private void bufferTest() {
		EmbeddedChannel channel = new EmbeddedChannel() ;
		channel.pipeline().addLast(new LengthFieldBasedFrameDecoder(44,4,4)) ;
		channel.pipeline().addLast(new RequestHandler()) ;
		//channel.pipeline().addLast(new RequestDecoder()) ;
		//channel.pipeline().addLast(new DiscardServerHandler()) ;

		CardModel model = CardModel.builder()
				.cardNo("5133310023055946")
				.amount(90000)
				.vat(9000)
				.build() ;


        int len = 36 ;
		byte[] bytes = intToByteArray(len, 4) ;
		System.out.println ("LEN :" +  bytes.length);

		 int bodyLen = ByteBuffer.wrap(bytes).getInt() ;
		System.out.println ("bodyLen : " + bodyLen );

		ByteBuf buff = Unpooled.buffer() ;
		channel.writeInbound(Unpooled.buffer().writeBytes("0101".getBytes(StandardCharsets.UTF_8))) ;
		channel.writeInbound(Unpooled.buffer().writeBytes(bytes)) ;
		buff.writeBytes( ( model.toString()).getBytes(StandardCharsets.UTF_8)) ;
		channel.writeInbound(buff) ;
		//RequestModel requestModel = (RequestModel) channel.readInbound() ;

		//log.info("result : {}" , requestModel.buildMessage().toString());
	}

	public  byte[] intToByteArray(int value, int lengthDiv) {
		byte[] byteArray = new byte[lengthDiv];
		if (lengthDiv == 2){
			byteArray[0] = (byte) value;
			byteArray[1] = (byte) (value >>> 8);
		}else if (lengthDiv == 4){
			byteArray[0] = (byte)(value >> 24);
			byteArray[1] = (byte)(value >> 16);
			byteArray[2] = (byte)(value >> 8);
			byteArray[3] = (byte)(value);
		}
		return byteArray;
	}




}
