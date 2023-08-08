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

	ConnectionPool connectionPool;
	
	Logger logger = Logger.getLogger("Cargo Repository");
	
	public ItemRepository() {
		try {
			connectionPool = ConnectionPool.create();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	public Optional<Item> selectItem(Long itemId) throws Exception {

		Connection conn = connectionPool.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Item item = null;

		try {
			pstmt = conn.prepareStatement("SELECT * FROM item WHERE itemId=?");

			pstmt.setLong(1, itemId);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				item = Item.builder().itemId(rset.getLong(1)).categoryId(rset.getLong(2)).itemName(rset.getString(3))
						.itemPrice(rset.getLong(4)).build();
			}
		} catch (Exception e) {
			throw new Exception("아이템 조회 에러 ");
		} finally {
			CRUDRepository.closeResultSet(rset);
			CRUDRepository.closePreparedStatement(pstmt);
			connectionPool.releaseConnection(conn);
		}

		return Optional.of(item);
	}

	public Optional<Item> selectItem(Connection con, Long itemId) throws Exception {

		Item item = null;
		PreparedStatement pstmt = null;

		ResultSet rset = null;

		try {
			pstmt = con.prepareStatement("SELECT * FROM item WHERE itemId=?");

			pstmt.setLong(1, itemId);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				item = Item.builder().itemId(rset.getLong(1)).categoryId(rset.getLong(2)).itemName(rset.getString(3))
						.itemPrice(rset.getLong(4)).build();
			}
		} catch (Exception e) {
			throw new Exception("아이템 조회 에러 ");
		}

		return Optional.of(item);
	}

}
