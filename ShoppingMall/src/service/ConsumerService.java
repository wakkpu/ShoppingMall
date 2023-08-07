package service;

import java.util.List;
import java.util.Optional;

import Entity.Consumer;
import dto.JoinDto;
import dto.LoginDto;
import dto.LoginResultDto;
import repo.CRUDRepository;
import repo.ConsumerRepository;

public class ConsumerService {
	private ConsumerRepository consumerRepository = new ConsumerRepository();
	
	/**
	 * 회원가입하기
	 * @param joinDto
	 */
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
	 * 로그인이메일로 유저정보 찾아오기
	 * @param loginDto
	 */
	public LoginResultDto getUser(LoginDto loginDto) {
		Consumer found = null;
		LoginResultDto loginResultDto = null;
		try {
			found = consumerRepository.selectByEmail(loginDto.getLoginEmail());
			String grade = getLoginedMembership(found);
			loginResultDto = LoginResultDto.builder()
								.userEmail(found.getUserEmail())
								.password(found.getPassword())
								.userName(found.getUserName())
								.grade(grade)
								.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return loginResultDto;
	}
	
	/**
	 * 
	 */
	public String getLoginedMembership(Consumer logined) {
		String grade = null;
		try {
			grade = consumerRepository.selectMembershipById(logined.getConsumerId());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grade;
	}
	
	/** 
	 * 이메일에 해당하는 비밀번호가 맞는지 확인
	 * @param loginDto
	 * @return boolean
	 */
	public boolean checkUser(LoginDto loginDto) {
		LoginResultDto dbConsumer = getUser(loginDto);
		if(!loginDto.getLoginPwd().equals(dbConsumer.getPassword())) {
			return false;
		}
		return true;
	}
	
	
}
