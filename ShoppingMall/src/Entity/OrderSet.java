package Entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderSet {
	private Long orderSetId;
	private Long consumerId;
	private String orderCode;
	private LocalDateTime orderTime;
	private String orderAddress;

}
