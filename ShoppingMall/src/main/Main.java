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
			System.out.println("1. �α��� 2. ȸ������");
			String cmd = sc.next();
			if(cmd.equals("1")) {
				System.out.println("�α��� �̸����� �Է����ּ���");
				String loginEmail = sc.next();
				System.out.println("�α��� ��й�ȣ�� �Է����ּ���");
				String loginPwd = sc.next();
				LoginDto loginDto = new LoginDto(loginEmail, loginPwd);
				if(!consumerService.checkUser(loginDto)) {
					System.out.println("�̸��ϰ� ��й�ȣ�� ��ġ���� �ʽ��ϴ�. �ٽ� �Է����ּ���.");
					System.out.println("�α��� ��й�ȣ�� �Է����ּ���");
					loginPwd = sc.next();
					break;
				}
				loginedUser = consumerService.getUser(loginDto);
				if(!loginedUser.isAdmin()) {
					System.out.println("�ȳ��ϼ��� " + loginedUser.getUserName() +"�� ȯ���մϴ�~");
					System.out.println(loginedUser.getUserName() + "���� ���� ����� ����� " + loginedUser.getGrade() + "�Դϴ�.");
					System.out.println();
					cmd = sc.next();
					System.out.println("1. ��ٱ��� ��ȸ 2. ��ǰ ��� ��ȸ 3. �ֹ� ��� ��ȸ 4. �α׾ƿ�");
					if(cmd.equals("1")) {
						
					} else if(cmd.equals("2")) {
						
					} else if(cmd.equals("3")) {
						
					} else if(cmd.equals("4")) {
						loginedUser = null;
						continue;
					}
				} else { //������ �α���
					System.out.println("������(admin) �����Դϴ�");
					System.out.println("������ �۾��� �����ϼ���");
					cmd = sc.next();
					System.out.println("1. â�� ��� ��ȸ 2. ��ǰ ��� ��ȸ 3. �ֹ���� ��ȸ 4. �α׾ƿ�");
					if(cmd.equals("1")) {
						
					} else if(cmd.equals("2")) {
						
					} else if(cmd.equals("3")) {
						
					} else if(cmd.equals("4")) {
						loginedUser = null;
						continue;
					}
				}
				
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
				String userName = sc.next();
				JoinDto joinDto = new JoinDto(++lastId, userEmail, password, phoneNumber, address, userName);
				consumerService.register(joinDto);
			}
		}
		
	}

}
