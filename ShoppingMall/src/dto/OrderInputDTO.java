package dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class OrderInputDTO {

    private long itemId;
    private long itemQuantity;

}