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
	 * email을 통해 해당 유저를 찾고 추가 정보를 합해 LoginResultDto 반환
	 * @param loginDto
	 * @return LoginResultDto
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
	 * 로그인된 user의 memebership정보 중 grade 정보 가져오기
	 * @param logined
	 * @return
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
	 * 로그인 시, user가 입력한 password가 db정보와 맞는지 확인
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
	
	/**
	 * consumerId를 통해 해당 user의 membership정보 가져오기
	 * @param consumerId
	 * @return membershipDto
	 */
	public MembershipDto getMembershipDetail(Long consumerId) {
		MembershipDto membershipDto = null;
		try {
			Membership membership = consumerRepository.selectMembershipById(consumerId);
			membershipDto = MembershipDto.builder()
								.grade(membership.getGrade())
								.discountRate(membership.getDiscountRate())
								.build();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return membershipDto;
	}
	
	
}
