package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CancelInput {
	
	private long itemId;
	private long modifyQuantity;	


}
