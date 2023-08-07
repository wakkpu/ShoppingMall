package service;

import java.util.List;
import java.util.Optional;

import Entity.Consumer;
import dto.JoinDto;
import dto.LoginDto;
import repo.CRUDRepository;
import repo.ConsumerRepository;

public class ConsumerService {
	private ConsumerRepository consumerRepository = new ConsumerRepository();
	
	public void register(JoinDto joinDto) {
		Consumer newcomer = new Consumer(joinDto.getConsumerId(), 1L, joinDto.getUserEmail(),
											joinDto.getPassword(), joinDto.getPhoneNumber(),joinDto.getAddress(), joinDto.getUserName(), false);
		try {
			consumerRepository.insert(newcomer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 * �̸��Ͽ� �ش��ϴ� ��й�ȣ�� �´��� Ȯ��
	 * @param loginDto
	 * @return
	 */
//	public boolean checkUser(LoginDto loginDto) {
//		Consumer dbConsumer = consumerRepository.selectByEmail(loginDto.getLoginEmail());
//		if(!loginDto.getLoginPwd().equals(dbConsumer.getPassword())) {
//			return false;
//		}
//		return true;
//	}
//	
//	public Consumer getUser(LoginDto loginDto) {
//		return consumerRepository.selectByEmail(loginDto.getLoginEmail());
//	}
}
