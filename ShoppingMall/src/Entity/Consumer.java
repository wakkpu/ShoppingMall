package Entity;

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
public class Consumer {
	private Long consumerId;
	private Long membershipId;
	private String userEmail;
	private String password;
	private String phoneNumber;
	private String userName;
	private boolean isAdmin;
}
