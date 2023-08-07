package Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Status {
	private Long statusId;
	private String statusName;
	private Long masterStatusId;
}
