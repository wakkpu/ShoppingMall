package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Entity.Consumer;
import Entity.OrderSet;
import dto.CancelInput;
import dto.LoginResultDto;
import dto.OrderDetailDto;
import dto.OrderDetailResponse;
import dto.OrderInputDTO;
import service.OrderService;

public class OrderMain {

	static Consumer consumer = new Consumer(1L, 1L, "c1@naver.com", "123", "010-2234-5522", "관악구", "최소영", false);
	
	static LoginResultDto loginedUser = null;
	static OrderService orderService = new OrderService();

	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);

		System.out.println("장바구니 목록");
		
		// 1. 장바구니 목록 보기
		
		System.out.println("1. 전체 주문 2. 부분 주문");
		String cmd = sc.next();
        
		// 2. 주문 하기
		// 주문 dto
		List<OrderInputDTO> orderInputList = new ArrayList<OrderInputDTO>();
		
		// 전체 주문 - 데이터 넣기
		if (cmd.equals("1")) {
			
			
		} else if (cmd.equals("2")) { // 선택 주문 - 업데이트 적용해서 데이터 넣기
			
		}

		int orderRset = orderService.order(consumer.getConsumerId(), orderInputList, "관악구");
		
		if (orderRset == 1) {
			System.out.println("주문에 성공하셨습니다.");
		}
		
		// 3.
		System.out.println("1. 장바구니 조회 2. 상품 목록 조회 3. 주문 목록 조회");
		
		cmd = sc.next();
		
		if(cmd.equals("1")) {
			
		} else if(cmd.equals("2")) {

		} else if(cmd.equals("3")) {
			
			List<OrderSet> orderList = orderService.readOrderList(consumer.getConsumerId());
			int n = 1;
			for (OrderSet o : orderList) {
				System.out.println(n + ". 주문 코드 : " + o.getOrderCode() + "주문 시간 : " + o.getOrderTime() + "배송지 : " + o.getOrderAddress());
				n++;
			}
			
			// 4.
			System.out.println("특정 주문을 상세히 보고 싶다면, 해당 번호를 입력하세요.");
			int idx = Integer.parseInt(sc.next())-1;
			
			OrderDetailResponse orderDetail = orderService.readOrderDetail(orderList.get(idx).getOrderSetId());
			int itemIdx = 1;
			
			for (OrderDetailDto o : orderDetail.getOrderDetail()) {
				
				System.out.println(itemIdx + "상품 명 : " + o.getItemName() + "수량 : " + o.getItemQuantity() + "구매 가격 :  " + o.getItemPrice());
				itemIdx++;
			}
			
			System.out.println("총 금액 : " + orderDetail.getTotalPrice());
			
			// 5.
			System.out.println("1. 주문 전체 취소 2. 주문 변경");
			
			cmd = sc.next();
			
			// 취소 dto
			List<CancelInput> cancelInputList = new ArrayList<CancelInput>();
			 
			// 전체 취소 - 데이터 넣기
			if (cmd.equals("1")) {
				
				for (OrderDetailDto o : orderDetail.getOrderDetail()) {
					cancelInputList.add(new CancelInput(o.getItemId(), 0));
				}
				
			} else if(cmd.equals("2")) { // 선택 취소 - 업데이트 적용해서 데이터 넣기
				System.out.println("아이템 순서대로 변경할 수량을 입력하세요. ");

				for (OrderDetailDto o : orderDetail.getOrderDetail()) {
					cancelInputList.add(new CancelInput(o.getItemId(), Integer.parseInt(sc.next())));
				}	
				
//				idx = Integer.parseInt(sc.next())-1;				
//				long itemId = orderDetail.getOrderDetail().get(idx).getItemId();
//				for (OrderDetailDto o : orderDetail.getOrderDetail()) {
//					cancelInputList.add(new CancelInput(o.getItemId(), o.getItemQuantity()));
//				}
				
			}
			
			int updateOrderRset = orderService.cancelOrder(cancelInputList);
			
			if (updateOrderRset == 1) {
				System.out.println("취소에 성공하셨습니다.");
			}
			
			
			
			
		} 
		
		
	}

}
