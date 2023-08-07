package service;

import java.util.List;
import java.util.Optional;

import Entity.Consumer;
import dto.LoginDto;
import repo.CRUDRepository;
import repo.ConsumerRepository;

public class ConsumerService {
	private ConsumerRepository consumerRepository = new ConsumerRepository();
	public boolean checkUser(LoginDto loginDto) {
		Consumer test = new Consumer();
		`	
		//Consumer dbConsumer = consumerRepository.selectByEmail(loginDto.);
		if(!loginDto.getLoginPwd().equals(dbConsumer.getPassword())) {
			return false
		}
		return true;
	}
	public Consumer getUser(LoginDto loginDto) {
		return consumerRepository.selectByEmail(loginDto.getLoginEmail);
	}
}
