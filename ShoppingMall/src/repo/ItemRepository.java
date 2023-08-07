package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import Entity.Item;
import util.ConnectionPool;

public class ItemRepository {

	ConnectionPool cp;

	public ItemRepository() {
		try {
			cp = ConnectionPool.create();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Optional<Item> selectItem(Long itemId) throws Exception {

		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Item item = null;

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
		} finally {
			CRUDRepository.closeRset(rset);
			CRUDRepository.closePstmt(pstmt);
			cp.releaseConnection(con);
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
