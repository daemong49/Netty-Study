package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void bufferTest() {
		ByteBuffer buffer = ByteBuffer.allocate(11);

		System.out.println("test11111");
		assertEquals(0, buffer.position());
		assertEquals(11, buffer.limit());

		buffer.put((byte)1);
		buffer.put((byte)2);
		buffer.put((byte)3);
		buffer.put((byte)4);

		assertEquals(4, buffer.position());
		assertEquals(11, buffer.limit());

		buffer.flip();
		assertEquals(0, buffer.position());
		assertEquals(4, buffer.limit());
	}

}
