package service;

import Entity.Item;
import dto.CategoryDataDto;
import repo.ItemRepository;

import java.util.List;

public class ItemService {
    ItemRepository itemRepository = new ItemRepository();

    public List<Item> selectAll() {
        return itemRepository.select("select * from item");
    }

    public List<Item> selectWithIn(CategoryDataDto categoryDataDto) {
        StringBuilder query = new StringBuilder();
        query.append("select * from item ");
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
        return itemRepository.select(query.toString());
    }
}
