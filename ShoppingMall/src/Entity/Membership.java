package Entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Membership {
	private Long membershipId;
	private String grade;
	private double discountRate;
	private int requirement;
}
