package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class LoginResultDto {
	private Long consumerId;
	private Long membershipId;
	private String userEmail;
	private String password;
	private String phoneNumber;
	private String address;
	private String userName;
	private String grade;
	private boolean isAdmin;
}
