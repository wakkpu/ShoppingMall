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
        return categoryRepository.selectAll();
    }

    public List<Item> selectWithCategoryId(Long categoryId){
        return itemService.selectWithIn(categoryRepository.selectWithCategoryId(categoryId));
    }

    public List<CategoryDto> getChildCategoryId(Long categoryId){
        return categoryRepository.getChildCategory(categoryId);
    }

}
