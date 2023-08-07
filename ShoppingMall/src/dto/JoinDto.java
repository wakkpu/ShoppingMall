package dto;

import Entity.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Builder
public class JoinDto {
	private Long consumerId;
	private String userEmail;
	private String password;
	private String phoneNumber;
	private String address;
	private String userName;
}
