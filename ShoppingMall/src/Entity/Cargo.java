package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Cargo {
	private Long cargoId;
	private long itemId;
	private long statusId;
}
