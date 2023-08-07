package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class OrderDetail {
	
	private Long orderDetailId;
	private Long orderSetId;
	private Long cargoId;
	private Long statusId;
	private Long buyPrice;

}
