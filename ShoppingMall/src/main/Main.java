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
			List<Category> categories = categoryService.selectAll();

			for(Category category : categories){
				System.out.print(category.getCategoryId()+" "+category.getCategoryName()+" ");
			}
			System.out.println();

			while(true) {
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
import java.util.Scanner;

import Entity.Consumer;
import dto.JoinDto;
import dto.LoginDto;
import dto.LoginResultDto;
import dto.MembershipDto;
import service.ConsumerService;

public class Main {
	static LoginResultDto loginedUser = null;
	static ConsumerService consumerService = new ConsumerService();
	static long lastId = 1;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("1. 로그인 2. 회원가입");
			String cmd = sc.next();
			if(cmd.equals("1")) {
				System.out.println("로그인 이메일을 입력해주세요");
				String loginEmail = sc.next();
				System.out.println("로그인 비밀번호를 입력해주세요");
				String loginPwd = sc.next();
				LoginDto loginDto = new LoginDto(loginEmail, loginPwd);
				if(!consumerService.checkUser(loginDto)) {
					System.out.println("이메일과 비밀번호가 일치하지 않습니다. 다시 입력해주세요.");
					System.out.println("로그인 비밀번호를 입력해주세요");
					loginPwd = sc.next();
					break;
				}
				loginedUser = consumerService.getUser(loginDto);
				if(!loginedUser.isAdmin()) {
					System.out.println("안녕하세요 " + loginedUser.getUserName() +"님 환영합니다~");
					System.out.println(loginedUser.getUserName() + "님의 현재 멤버십 등급은 " + loginedUser.getGrade() + "입니다.");
					System.out.println();
					System.out.println("0. 회원 정보 조회 1. 장바구니 조회 2. 상품 목록 조회 3. 주문 목록 조회 4. 로그아웃");
					cmd = sc.next();
					if(cmd.equals("0")) {
						MembershipDto membership = consumerService.getMembershipDetail(loginedUser.getConsumerId());
						System.out.println("== " + loginedUser.getUserName() + "님의 회원정보 조회 ==");
						System.out.println("사용자명: " + loginedUser.getUserName());
						System.out.println("이메일: " + loginedUser.getUserEmail());
						System.out.println("전화번호: " + loginedUser.getPhoneNumber());
						System.out.println("기본 배송지: " + loginedUser.getAddress());
						System.out.println("*멤버십: " + membership.getGrade() + " (할인률: " + membership.getDiscountRate() + "% 적용)");
					} else if(cmd.equals("1")) {
						
					} else if(cmd.equals("2")) {
						
					} else if(cmd.equals("3")) {
						
					} else if(cmd.equals("4")) {
						loginedUser = null;
						continue;
					}
				} else { //관리자 로그인
					System.out.println("관리자(admin) 계정입니다");
					System.out.println("수행할 작업을 선택하세요");
					cmd = sc.next();
					System.out.println("1. 창고 목록 조회 2. 상품 목록 조회 3. 주문목록 조회 4. 로그아웃");
					if(cmd.equals("1")) {
						
					} else if(cmd.equals("2")) {
						
					} else if(cmd.equals("3")) {
						
					} else if(cmd.equals("4")) {
						loginedUser = null;
						continue;
					}
				}
				
			} else if(cmd.equals("2")) {
				System.out.println("가입하실 이메일을 입력해주세요");
				boolean checkValid = false;
				String userEmail = null;
				while(!checkValid) {
					userEmail = sc.next();
					//이메일 형식 확인
					break;//임시
				}
				System.out.println("가입하실 비밀번호를 입력해주세요");
				String password = sc.next();
				
				System.out.println("사용자의 전화번호를 입력해주세요");
				String phoneNumber = sc.next();
				
				System.out.println("사용자의 주소를 입력해주세요");
				String address = sc.next();
				
				System.out.println("사용자의 이름을 설정해주세요");
				String userName = sc.next();
				JoinDto joinDto = new JoinDto(++lastId, userEmail, password, phoneNumber, address, userName);
				consumerService.register(joinDto);
			}
		}
	
	}

}
