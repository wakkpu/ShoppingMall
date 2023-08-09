package repo;

import Entity.Category;
import Entity.Item;
import dto.CategoryDataDto;
import dto.CategoryDto;

import java.sql.Connection;
import java.util.List;

public class CategoryRepository {
    RowMapper categoryRowMapper;
    RowMapper categoryDataDtoRowMapper;
    RowMapper categoryDtoRowMapper;
    CRUDTemplate<Item> crudTemplate = new CRUDTemplate<>();
    CRUDTemplate<CategoryDataDto> categoryDataDtoCRUDTemplate = new CRUDTemplate<>();
    CRUDTemplate<CategoryDto> categoryDtoTemplate = new CRUDTemplate<>();
    public CategoryRepository(){
        categoryRowMapper = rset->Category.builder()
                .categoryId(rset.getLong("category_id"))
                .categoryName(rset.getString("category_name"))
                .masterCategoryId(rset.getLong("master_category_id"))
                .build();
        categoryDataDtoRowMapper = rset -> CategoryDataDto.builder()
                .categoryId1(rset.getLong("category_id1"))
                .categoryId2(rset.getLong("category_id2"))
                .categoryId3(rset.getLong("category_id3"))
                .build();
        categoryDtoRowMapper = rset -> CategoryDto.builder()
                .categoryId(rset.getLong("category_id"))
                .categoryName(rset.getString("category_name"))
                .build();
    }
    public List<Category> select(String query){
        return crudTemplate.select(query,categoryRowMapper);
    }

    public CategoryDataDto selectWithCategoryId(String query){
        return categoryDataDtoCRUDTemplate.selectOne(query,categoryDataDtoRowMapper);
    }

    public List<CategoryDto> getChildCategory(String query){
        return categoryDtoTemplate.select(query,categoryDtoRowMapper);
    }

    public int insertCategory(String query){
        return categoryDtoTemplate.insert(query);
    }
}