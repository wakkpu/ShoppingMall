package Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cargo {
	private Long cargoId;
	private long itemId;
	private long statusId;
}
