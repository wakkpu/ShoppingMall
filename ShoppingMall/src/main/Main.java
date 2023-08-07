package main;

import java.util.Scanner;

import Entity.Consumer;
import dto.JoinDto;
import dto.LoginDto;
import dto.LoginResultDto;
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
					cmd = sc.next();
					System.out.println("1. 장바구니 조회 2. 상품 목록 조회 3. 주문 목록 조회 4. 로그아웃");
					if(cmd.equals("1")) {
						
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
