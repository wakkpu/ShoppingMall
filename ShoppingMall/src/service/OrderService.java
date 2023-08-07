package service;

import java.util.List;
import java.util.Optional;

import Entity.Consumer;
import Entity.Item;
import Entity.OrderDetail;
import Entity.OrderSet;
import dto.CancelInput;
import dto.OrderDetailResponse;
import dto.OrderInputDTO;
import repo.CargoRepository;
import repo.ItemRepository;
import repo.OrderDetailRepository;
import repo.OrderRepositoty;
import repo.OrderSetRepository;

public class OrderService {

	OrderSetRepository orderSetRepository ;
	OrderDetailRepository orderDeatailRepository;
	CargoRepository cargoRepository;
	ItemRepository itemRepository;
	OrderRepositoty orderRepository;

	public OrderService() {
		
		orderRepository = new OrderRepositoty();
		orderSetRepository  = new OrderSetRepository();
		orderDeatailRepository  = new OrderDetailRepository();
		cargoRepository = new CargoRepository();
		itemRepository = new ItemRepository();
		
	}

	/** 바로 주문
	 * @throws Exception 
	 * 
	 * 
	 * */
	public int order(Long consumerId, List<OrderInputDTO> orderInputList, String orderAddress) throws Exception {
		
		return orderRepository.order(consumerId, orderInputList, orderAddress);
		

	}


	/**
	 * 주문 목록 조회
	 */
	
	public List<OrderSet> readOrderList(Consumer consumer) throws Exception {
		
		List<OrderSet> result = orderRepository.selectOrderListByConsumerId(consumer.getConsumerId());
		
		return result;

	}
	
	/**
	 * 특정 주문 상세 조회
	 */
	
	public OrderDetailResponse readOrderList(Long orderSetId) throws Exception {
		
		
		List<OrderDetail> orderDetailList = orderRepository.selectDetial(orderSetId);
		OrderDetailResponse response = new OrderDetailResponse();
		List<OrderInputDTO> orderDetail = null;
		long totalPrice = 0;
		for (OrderDetail orderDetial : orderDetailList) {
			
			long itemId = cargoRepository.selectItem(orderDetial.getCargoId());
			Item item = itemRepository.selectItem(itemId).get();
			long price = orderDeatailRepository.selectPrice(orderDetial.getCargoId());
			totalPrice += price;
			
			orderDetail.add(new OrderInputDTO(item.getItemId(), price));
		}
		response.setOrderDetail(orderDetail);
		response.setTotalPrice(totalPrice);
		
		return response;

	}
	
	/**
	 * 주문 변경 및 취소
	 * */
	public int cancelOrder(List<CancelInput> cancelInputList) throws Exception {
		
		return orderRepository.deleteOrder(cancelInputList);
		
	}
	

}
