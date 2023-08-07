package service;

import java.util.List;

import Entity.Item;
import Entity.OrderDetail;
import Entity.OrderSet;
import dto.CancelInput;
import dto.ItemQuntityDto;
import dto.OrderDetailDto;
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

	/** 주문
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
	
	public List<OrderSet> readOrderList(Long consumerId) throws Exception {
		
		List<OrderSet> result = orderRepository.selectOrderListByConsumerId(consumerId);
		
		return result;

	}
	
	/**
	 * 특정 주문 상세 조회
	 */
	
	public OrderDetailResponse readOrderDetail(Long orderSetId) throws Exception {
		
		
		List<ItemQuntityDto> orderDetailList = orderRepository.selectDetial(orderSetId);
		OrderDetailResponse response = new OrderDetailResponse();
		List<OrderDetailDto> orderDetail = null;
		long totalPrice = 0;
		
		for (ItemQuntityDto i : orderDetailList) {
			
			String itemName = itemRepository.selectItem(i.getItemId()).get().getItemName();
			totalPrice += i.getItemPrice();
			
			orderDetail.add(new OrderDetailDto(i.getItemId(), itemName, i.getItemQuantity(), i.getItemPrice()));
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
