package repo;

import Entity.Item;
import java.util.List;

public class ItemRepository {
    CRUDTemplate<Item> crudTemplate = new CRUDTemplate<>();
    RowMapper rowMapper;
    public ItemRepository(){
        rowMapper = rset -> Item.builder()
                .itemId(rset.getLong("item_id"))
                .itemName(rset.getString("item_name"))
                .itemPrice(rset.getLong("item_price"))
                .build();
    }

    public List<Item> select(String query){
        return crudTemplate.select(query,rowMapper);
    }
}
