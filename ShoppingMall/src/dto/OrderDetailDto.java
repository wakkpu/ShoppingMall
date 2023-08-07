package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OrderDetailDto {
	
	private Long itemId;
	private String itemName;
	private long itemQuantity;
	private long itemPrice;	

}
