package Entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartItem {
	private Long itemId;
	private Long consumerId;
	private long itemQuantity;
}
