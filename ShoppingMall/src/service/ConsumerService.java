package service;

import java.util.List;
import java.util.Optional;

import Entity.Consumer;
import Entity.Membership;
import dto.JoinDto;
import dto.LoginDto;
import dto.LoginResultDto;
import dto.MembershipDto;
import repo.CRUDRepository;
import repo.ConsumerRepository;

public class ConsumerService {
	private ConsumerRepository consumerRepository = new ConsumerRepository();
	
	/**
	 * 
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
	 * @param loginDto
	 */
	public LoginResultDto getUser(LoginDto loginDto) {
		Consumer found = null;
		LoginResultDto loginResultDto = null;
		try {
			found = consumerRepository.selectByEmail(loginDto.getLoginEmail());
			String grade = getLoginedMembership(found);
			loginResultDto = LoginResultDto.builder()
								.consumerId(found.getConsumerId())
								.membershipId(found.getMembershipId())
								.userEmail(found.getUserEmail())
								.password(found.getPassword())
								.phoneNumber(found.getPhoneNumber())
								.address(found.getAddress())
								.userName(found.getUserName())
								.grade(grade)
								.isAdmin(found.isAdmin())
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
			Membership membership = consumerRepository.selectMembershipById(logined.getConsumerId());
			grade = membership.getGrade();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return grade;
	}
	
	/** 
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
	
	public MembershipDto getMembershipDetail(Long consumerId) {
		MembershipDto membershipDto = null;
		try {
			Membership membership = consumerRepository.selectMembershipById(consumerId);
			membershipDto = MembershipDto.builder()
								.grade(membership.getGrade())
								.discountRate(membership.getDiscountRate())
								.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return membershipDto;
	}
	
	
}
