package dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CartItemDto {
    private Long itemId;
    private String itemName;
    private long itemPrice;
    private long itemQuantity;
}
