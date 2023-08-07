package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CargoDto {
	private Long cargoId;
	private String itemName;
	private String statusName;
	private Long itemPrice;
	private Long cargoCount;
}
