package repo;

import Entity.Item;
import dto.CartItemDto;
import resources.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    CRUDTemplate<Item> crudTemplate = new CRUDTemplate<>();
    ConnectionPool cp;
    public ItemRepository(){
        try {
            cp = ConnectionPool.create();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> selectAll(){
        RowMapper rowMapper = rset -> Item.builder()
                .itemId(rset.getLong("item_id"))
                .itemName(rset.getString("item_name"))
                .itemPrice(rset.getLong("item_price"))
                .build();
        return crudTemplate.selectAll("select * from item",rowMapper);
        /*ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        List<Item> itemList = new ArrayList<>();
        try {
            pstmt = con.prepareStatement("select * from item");
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
            cp.releaseConnection(con);
        }
        return itemList;*/
    }

    public List<Item> selectWithIn(String condition){
        ResultSet rset = null;
        Connection con = cp.getConnection();
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
            cp.releaseConnection(con);
        }
        return itemList;
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
