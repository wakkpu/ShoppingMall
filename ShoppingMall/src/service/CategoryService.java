package service;

import Entity.Category;
import Entity.Item;
import dto.CategoryDataDto;
import dto.CategoryDto;
import repo.CategoryRepository;
import repo.ItemRepository;

import java.util.List;

public class CategoryService {
    CategoryRepository categoryRepository = new CategoryRepository();
    ItemService itemService = new ItemService();

    public List<Category> selectAll(){
        return categoryRepository.select("select * from category where master_category_id is null ");
    }

    public List<Item> selectWithCategoryId(Long categoryId){
        StringBuilder query = new StringBuilder();
        query.append("SELECT child.category_id AS category_id1, grandChild.category_id AS category_id2,parent.category_id AS category_id3 FROM category parent LEFT JOIN category child ON parent.category_id = child.master_category_id LEFT JOIN category grandChild ON child.category_id = grandChild.master_category_id WHERE parent.category_id = ");
        query.append(categoryId);
        return itemService.selectWithIn(categoryRepository.selectWithCategoryId(query.toString()));
    }

    public List<CategoryDto> getChildCategoryId(Long categoryId){
        StringBuilder query = new StringBuilder();
        query.append("SELECT child.category_id, child.category_name FROM category parent LEFT JOIN category child ON parent.category_id = child.master_category_id WHERE parent.category_id = ");
        query.append(categoryId);
        return categoryRepository.getChildCategory(query.toString());
    }

}
