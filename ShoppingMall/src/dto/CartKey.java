package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartKey{
    private Long itemId;
    private Long consumerId;
}