package service;

import Entity.Category;
import Entity.Item;
import dto.CategoryDataDto;
import repo.CartRepository;
import repo.ItemRepository;

import java.util.List;

public class ItemService {
    ItemRepository itemRepository = new ItemRepository();

    public List<Item> selectAll() {
        return itemRepository.selectAll();
    }

    public List<Item> selectWithIn(CategoryDataDto categoryDataDto) {
        StringBuilder query = new StringBuilder();
        query.append("where category_id=");
        query.append(categoryDataDto.getCategoryId3());
        if (categoryDataDto.getCategoryId1() != 0L) {
            query.append(" or category_id = ");
            query.append(categoryDataDto.getCategoryId1());
        }
        if (categoryDataDto.getCategoryId2() != 0L) {
            query.append(" or category_id = ");
            query.append(categoryDataDto.getCategoryId2());
        }
        System.out.println(query);
        return itemRepository.selectWithIn(query.toString());
    }
}
