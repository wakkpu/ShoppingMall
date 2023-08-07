package Entity;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderSet {
	private Long orderSetId;
	private Long consumerId;
	private String orderCode;
	private LocalDateTime orderTime;
	private String orderAddress;

}
