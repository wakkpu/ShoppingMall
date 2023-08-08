package test;

import Entity.Item;
import org.junit.jupiter.api.Test;
import repo.CartRepository;
import repo.CategoryRepository;
import repo.ItemRepository;

public class CartRepositoryTest extends RootTest{

	CartRepository cartRepository;
    Long itemId;


	@Test
	void insertTest() {
        insertDummyData();
        // 카테고리 저장 -> 해당 카테고리 값 가져오기
        // 상품 저장(카테고리 값 저장)
        // 카트 저장


        //SELECT IFNULL(MAX(item_id)+1,1) FROM item;
        //Item item = Item.builder().

        //CartItem cartItem = CartItem.builder().itemId()
        //cartRepository.insert();
	}

    private void insertDummyData() {
        cartRepository = new CartRepository();
        ItemRepository itemRepository = new ItemRepository();
        CategoryRepository categoryRepository = new CategoryRepository();

        Long categoryId = crudTemplate.selectOneColumn("select MAX(category_id) + 1 as category_id from category", Long.class);
        categoryRepository.insertCategory(connection,"INSERT INTO category SELECT IFNULL(MAX(category_id) + 1, 1), '최성훈', NULL FROM category");

        itemRepository.insertItem(connection,Item.builder().categoryId(categoryId).itemName("최성훈").itemPrice(50000).build());
        Long itemId = crudTemplate.selectOneColumn("select MAX(item_id) as category_id from item", Long.class);
    }


}