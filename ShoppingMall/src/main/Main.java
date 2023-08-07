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
			System.out.println("1. �α���(1) 2. ȸ������ (2)");
			String cmd = sc.next();
			if(cmd.equals("1")) {
//				System.out.println("�α��� �̸����� �Է����ּ���");
//				String loginEmail = sc.next();
//				System.out.println("�α��� ��й�ȣ�� �Է����ּ���");
//				String loginPwd = sc.next();
//				LoginDto loginDto = new LoginDto(loginEmail, loginPwd);
//				if(!consumerService.checkUser(loginDto)) {
//					System.out.println("�̸��ϰ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
//				}
//				Consumer user = consumerService.getUser(loginDto);
			} else if(cmd.equals("2")) {
				System.out.println("�����Ͻ� �̸����� �Է����ּ���");
				boolean checkValid = false;
				String userEmail = null;
				while(!checkValid) {
					userEmail = sc.next();
					//�̸��� ���� Ȯ��
					break;//�ӽ�
				}
				System.out.println("�����Ͻ� ��й�ȣ�� �Է����ּ���");
				String password = sc.next();
				
				System.out.println("������� ��ȭ��ȣ�� �Է����ּ���");
				String phoneNumber = sc.next();
				
				System.out.println("������� �ּҸ� �Է����ּ���");
				String address = sc.next();
				
				System.out.println("������� �̸��� �������ּ���");
				System.out.print("�̸�: ");
				String userName = sc.next();
				JoinDto joinDto = new JoinDto(++lastId, userEmail, password, phoneNumber, address, userName);
				consumerService.register(joinDto);
			}
		}
		
	}

}
