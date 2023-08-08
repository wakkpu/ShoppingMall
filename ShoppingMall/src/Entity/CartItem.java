package Entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CartItem {
	private Long itemId;
	private Long consumerId;
	private long itemQuantity;
}
