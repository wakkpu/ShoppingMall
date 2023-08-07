package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderInputDTO {
	
	private long itemId;
	private long itemQuantity;	

}
