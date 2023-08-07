package Entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Consumer {
	private Long consumerId;
	private Long membershipId;
	private String userEmail;
	private String password;
	private String phoneNumber;
	private String userName;
	private boolean isAdmin;
	
	
}
