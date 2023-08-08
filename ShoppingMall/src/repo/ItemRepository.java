package repo;


import Entity.Item;
import resources.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import Entity.Item;
import resources.ConnectionPool;

public class ItemRepository {
  CRUDTemplate<Item> crudTemplate = new CRUDTemplate<>();
  ConnectionPool connectionPool;
	
	Logger logger = Logger.getLogger("Cargo Repository");
  RowMapper rowMapper;

    public ItemRepository(){
      try {
        connectionPool = ConnectionPool.create();
      } catch(SQLException e) {
        e.printStackTrace();
      }
          rowMapper = rset -> Item.builder()
                  .itemId(rset.getLong("item_id"))
                  .itemName(rset.getString("item_name"))
                  .itemPrice(rset.getLong("item_price"))
                  .build();
    }

    public List<Item> selectWithIn(String condition){
        ResultSet rset = null;
        Connection con = connectionPool.getConnection();
        PreparedStatement pstmt = null;
        List<Item> itemList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select * from item ");
            sb.append(condition);
            pstmt = con.prepareStatement(sb.toString());
            rset = pstmt.executeQuery();
            while(rset.next()){
                Item item = Item.builder()
                        .itemId(rset.getLong("item_id"))
                        .itemName(rset.getString("item_name"))
                        .itemPrice(rset.getLong("item_price"))
                        .build();
                itemList.add(item);
            }
        }catch(Exception e){
            throw new RuntimeException("DB 조회에 실패 하였습니다.");
        }finally {
            try {
                CRUDRepository.closeRset(rset);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                CRUDRepository.closePstmt(pstmt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            connectionPool.releaseConnection(con);
        }
        return itemList;
    }
	

    public List<Item> select(String query){
        return crudTemplate.select(query,rowMapper);
    }

	public int insertItem(Connection connection, Item item){
		int result = 0;
		Connection con = connection;
		if(connection == null){
			connectionPool.getConnection();
		}

		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("insert into item(category_id,item_name,item_price) values(?,?,?)");
			pstmt.setLong(1, item.getCategoryId());
			pstmt.setString(2, item.getItemName());
			pstmt.setLong(3, item.getItemPrice());
			result = pstmt.executeUpdate();
		}catch(Exception e){
			throw new RuntimeException();
		}finally {
			try {
				CRUDRepository.closePstmt(pstmt);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			connectionPool.releaseConnection(con);
		}
		return result;
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
