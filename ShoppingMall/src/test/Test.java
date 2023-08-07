package test;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import Entity.Consumer;
import dto.OrderInputDTO;
import service.OrderService;

class Test {
	
	OrderService service;
	
	@BeforeEach
	void before() throws Exception {
		
		Consumer consumer = new Consumer(null, null, null, null, null, null, false);
		service = new OrderService();

	}

	@org.junit.Test
	void test() throws Exception {
		List<OrderInputDTO> orderInputList = new ArrayList<OrderInputDTO>();
		orderInputList.add(new OrderInputDTO(1, 4));
		orderInputList.add(new OrderInputDTO(2, 3));
	
		int result = service.order(1L, orderInputList, "관악구");
		
		System.out.println(result);
	
	}

}
