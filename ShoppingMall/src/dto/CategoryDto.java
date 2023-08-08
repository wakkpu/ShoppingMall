package dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryDto {
    Long categoryId;
    String categoryName;
}
