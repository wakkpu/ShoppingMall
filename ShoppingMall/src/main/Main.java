package main;

import java.util.Scanner;

import Entity.Consumer;
import dto.JoinDto;
import dto.LoginDto;
import service.ConsumerService;

public class Main {
	static Consumer loginedUser = null;
	static ConsumerService consumerService = new ConsumerService();
	static long lastId = 1;
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			System.out.println("1. 로그인(1) 2. 회원가입 (2)");
			String cmd = sc.next();
			if(cmd.equals("1")) {
//				System.out.println("로그인 이메일을 입력해주세요");
//				String loginEmail = sc.next();
//				System.out.println("로그인 비밀번호를 입력해주세요");
//				String loginPwd = sc.next();
//				LoginDto loginDto = new LoginDto(loginEmail, loginPwd);
//				if(!consumerService.checkUser(loginDto)) {
//					System.out.println("이메일과 비밀번호가 일치하지 않습니다.");
//				}
//				Consumer user = consumerService.getUser(loginDto);
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
				System.out.print("이름: ");
				String userName = sc.next();
				JoinDto joinDto = new JoinDto(++lastId, userEmail, password, phoneNumber, address, userName);
				consumerService.register(joinDto);
			}
		}
		
	}

}
