package test;

import Entity.Item;
import org.junit.jupiter.api.Test;
import repo.*;

public class CartRepositoryTest extends RootTest{
    CRUDTemplate<Item> crudTemplate = new CRUDTemplate<>();
	CartRepository cartRepository;
    Long itemId;
    Long consumerId;

	@Test
	void insertTest() {
        insertDummyData();

	}

    /*
        내장 db를 활용해서 독립적인 환경에서 구성할시 아래와 같은 더미 테이블은 필요가 없다.
        지금은 같은 환경에서 진행하므로 더미 데이터를 삽입하고 있으면 삭제하는 테스트 코드가 있어야한다.
        (테스트 코드는 몇번 실행하든 어떤 환경에서 실행하든 항상 같은 결과를 출력해야 하기 때문임)
     */
    private void insertDummyData() {
        cartRepository = new CartRepository();
        ItemRepository itemRepository = new ItemRepository();
        CategoryRepository categoryRepository = new CategoryRepository();

        Long categoryId = crudTemplate.fetchSingleValue("select IFNULL(MAX(category_id) + 1,1) as category_id from category", Long.class);
        categoryRepository.insertCategory("INSERT INTO category SELECT IFNULL(MAX(category_id) + 1, 1), '최성훈', NULL FROM category");

        itemRepository.insertItem(connection,Item.builder().categoryId(categoryId).itemName("최성훈").itemPrice(50000).build());
        itemId = crudTemplate.fetchSingleValue("select MAX(item_id) from item", Long.class);


        //ConsumerRepository consumerRepository = new ConsumerRepository();
        //consumerId = crudTemplate.selectOneColumn("select IFNULL(MAX(consumer_id) + 1,1) as consumer_id from consumer", Long.class);

//        MembershipRepository membershipRepository = new MembershipRepository();
//
//        membershipRepository.insertMembership(connection, "insert into membership select IFNULL(MAX(membership_id) + 1,1) as membership_id, 'CSH',30.0,0 from membership");
        //Consumer consumer = Consumer.builder().consumerId(consumerId).membershipId(null).userEmail("seonghun7304@naver.com").password("1234").phoneNumber("01012345678").address("집좀보내주세요").userName("최성훈").isAdmin(false).build();
        //consumerRepository.insert(connection, consumer);


    }
}