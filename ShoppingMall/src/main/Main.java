package main;

import Entity.Category;
import Entity.Item;
import dto.CartItemDto;
import dto.CategoryDto;
import dto.OrderInputDTO;
import service.CartService;
import service.CategoryService;
import service.ItemService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
	CartService cartService = new CartService();
	ItemService itemService = new ItemService();
	CategoryService categoryService = new CategoryService();

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	public static void main(String[] args) throws IOException {
		Main main = new Main();
		main.goItem();
		//main.goCart();
	}

	private void goItem() throws IOException {
		while(true) {
			System.out.println("1. 상품 목록 조회 | 2.종료");
			int choiceNumber = Integer.parseInt(br.readLine());
			if(choiceNumber==2) break;
			if (choiceNumber == 1) {
				searchItem();
			}
		}
	}

	public void searchItem() throws IOException {
		/*
				5. 상품 목록 조회 // read
				단순 조회 - 전체 상품 목록을 쭉 뿌리면
				조건별 검색 - 분류별 검색?
		 */
		System.out.println("1. 단순조회 | 2. 조건검색 | 3. 끝내기");
		long choiceNumber = Long.parseLong(br.readLine());
		if(choiceNumber==1){
			List<Item> items = itemService.selectAll();
			for(Item item : items){
				System.out.println(item.getItemName()+" "+item.getItemPrice());
			}
		}else if(choiceNumber==2){
			if(1==1) return;
			List<Category> categories = categoryService.selectAll();

			for(Category category : categories){
				System.out.print(category.getCategoryId()+" "+category.getCategoryName()+" ");
			}

			int count = 1;
			while(true) {
				if(count>3) break;
				System.out.println();
				System.out.println("1.보기 2.다음으로감(있으면) 3. 종료");
				choiceNumber = Integer.parseInt(br.readLine());

				if (choiceNumber == 1) {
					System.out.println("코드 입력");
					choiceNumber = Integer.parseInt(br.readLine());
					List<Item> items = categoryService.selectWithCategoryId(choiceNumber);
					for (Item item : items) {
						System.out.println(item.getItemName() + " " + item.getItemPrice());
					}
				} else if (choiceNumber == 2) {
					count++;
					System.out.println("상세 정보 볼 코드 입력");
					choiceNumber = Integer.parseInt(br.readLine());
					List<CategoryDto> childCategoryId = categoryService.getChildCategoryId(choiceNumber);
					for (CategoryDto categoryDto : childCategoryId) {
						System.out.println(categoryDto.getCategoryId()+" "+categoryDto.getCategoryName());
					}
				} else {
					break;
				}
			}
		}else{
			return;
		}
	}

	public void goCart() throws IOException {
		while(true) {
			System.out.println("1. 장바구니 담기 | 2. 장바구니 갯수 수정 | 3. 장바구니 삭제 | 4. 장바구니 조회 | 5. 주문하기 | 6. 그만보기");
			int choiceNumber = Integer.parseInt(br.readLine());

			switch(choiceNumber){
				case 1 : carryCart(); break;
				case 2 : updateCart(); break;
				case 3 : deleteCart(); break;
				case 4 : searchCart(); break;
				case 5 : orderCart(); break;
				default :  return;
			}
		}
	}

	private void carryCart() throws IOException {
		System.out.println("담고 싶은 상품 코드 입력 : ");
		cartService.addCart(Long.parseLong(br.readLine()));
		/*
			7. 장바구니에 담기
			장바구니에 있는거면 갯수++
			장바구니에 없던거면 장바구니 목록에 추가
		 */
	}

	private void updateCart() throws IOException {
		System.out.println("아이템 코드 입력:  ");
		Long itemCode = Long.parseLong(br.readLine());
		System.out.println("1. 더하기 2. 빼기");
		cartService.updateCart(Integer.parseInt(br.readLine()),itemCode);
		/*
		8. 장바구니 갯수 수정
		갯수--
		갯수 0이면 장바구니에서 자동으로 삭제
		 */
	}

	private void deleteCart() throws IOException {
		/*
			9. 장바구니 삭제
			부분 삭제 / 전체 삭제
		 */
		System.out.println("1. 부분 삭제 | 2. 전체 삭제");
		int input = Integer.parseInt(br.readLine());
		Long itemId = null;
		if(input==1){ // 부분 삭제
			System.out.println("삭제할 아이템 코드를 입력하세요");
			itemId = Long.valueOf(br.readLine());
		}
		cartService.delete(itemId);
	}

	private List<CartItemDto> searchCart() {
		System.out.println("코드 네임 가격 수량");
		List<CartItemDto> cartItemList = cartService.selectCart();
		for(CartItemDto cartItemDto : cartItemList){
			System.out.println(cartItemDto.getItemId()+" "+cartItemDto.getItemName()+" "+cartItemDto.getItemPrice()+" "+cartItemDto.getItemQuantity());
		}
		return cartItemList;
		/*
			10. 장바구니 조회
			어떤 제품이고, 몇개 담았는지 그 목록들을 보여줌
		 */
	}

	private void orderCart() throws IOException {
		List<CartItemDto> cartItemList = searchCart();

		System.out.println("1. 전체주문 | 2. 부분주문");
		int input = Integer.parseInt(br.readLine());

		List<OrderInputDTO> orderInputDTOList = new ArrayList<>();
		if(input==1){
			for (CartItemDto v : cartItemList) {
				OrderInputDTO orderInputDTO = OrderInputDTO.builder().itemId(v.getItemId()).itemQuantity(v.getItemQuantity()).build();
				orderInputDTOList.add(orderInputDTO);
			}
		}else if(input==2){
			Map<Long, Long> personMap = cartItemList.stream()
					.collect(Collectors.toMap(CartItemDto::getItemId, CartItemDto::getItemQuantity));

			System.out.println("코드 입력 : ");
			long key = Long.parseLong(br.readLine());
			OrderInputDTO orderInputDTO = OrderInputDTO.builder()
					.itemId(key)
					.itemQuantity(personMap.get(key))
					.build();
			orderInputDTOList.add(orderInputDTO);
		}

		System.out.println("배송지 입력 : ");
		String cargo = br.readLine();

		for(OrderInputDTO orderInputDTO : orderInputDTOList){
			System.out.println(orderInputDTO.getItemId()+" "+orderInputDTO.getItemQuantity());
		}
		System.out.println(cargo);
		//order();

		/*
				11. 장바구니 주문
				전체 주문 / 부분 주문
				주문시 배송지 입력 필요

				주문 내역 상세 생성 -> 구매 시점 가격 생성해서 fix 해줘야 함
		 */
	}
}
