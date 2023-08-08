package Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Status {
	private Long statusId;
	private String statusName;
	private Long masterStatusId;
}
