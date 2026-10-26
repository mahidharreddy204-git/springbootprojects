package com.example.order_service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Test 
	void testAddition(){
		int result = 10+20;
		assertEquals(30, result);
	}

}
