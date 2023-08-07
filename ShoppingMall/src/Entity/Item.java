package Entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Item {
	private Long itemId;
	private long categoryId;
	private String itemName;
	private long itemPrice;
}
