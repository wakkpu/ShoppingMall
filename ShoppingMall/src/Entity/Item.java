package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Item {
	private Long itemId;
	private long categoryId;
	private String itemName;
	private long itemPrice;
}
