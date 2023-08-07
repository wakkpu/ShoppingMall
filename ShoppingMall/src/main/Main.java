package main;

import java.util.Scanner;

import Entity.Consumer;
import dto.LoginDto;
import service.ConsumerService;

public class Main {
	static Consumer loginedUser = null;
	static ConsumerService consumerService = new ConsumerService();
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		while(true) {
			String cmd = sc.next();
			System.out.println("1. �α���(1) 2. ȸ������ (2)");
			if(cmd.equals("1")) {
				System.out.println("�α��� �̸����� �Է����ּ���");
				String loginEmail = sc.next();
				System.out.println("�α��� ��й�ȣ�� �Է����ּ���");
				String loginPwd = sc.next();
				LoginDto loginDto = new LoginDto(loginEmail, loginPwd);
				if(!consumerService.checkUser(loginDto)) {
					System.out.println("�̸��ϰ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�.");
				}
				Consumer user = consumerService.getUser(loginDto);
			} else if(cmd.equals("2")) {
				System.out.println("�����Ͻ� �̸����� �Է����ּ���");
				boolean checkValid = false;
				while(!checkValid) {
					String email = sc.next();
					//�̸��� ���� Ȯ��
				}
				System.out.println("�����Ͻ� ��й�ȣ�� �Է����ּ���");
				String password = sc.next();
				
				System.out.println("������� �̸��� �������ּ���");
				System.out.print("�̸�: ");
				String userName = sc.next();
			}
		}
		
	}

}
