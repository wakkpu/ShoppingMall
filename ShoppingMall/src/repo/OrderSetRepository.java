package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import Entity.OrderSet;
import resources.ConnectionPool;

public class OrderSetRepository{

	ConnectionPool connectionPool;
	
	Logger logger = Logger.getLogger("Cargo Repository");
	
	public OrderSetRepository() {
		try {
			connectionPool = ConnectionPool.create();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	// 주문 정보 삽입 후, orderId 반환
	public long insertSetOrder(Connection con, OrderSet v) throws Exception {

		int result = 0;
		long orderId = -1; //
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement("insert into order_set values(?, ?, ?, ?)");

			pstmt.setLong(1, v.getConsumerId());
			pstmt.setString(2, v.getOrderCode());
			pstmt.setString(3, v.getOrderTime().toString());
			pstmt.setString(4, v.getOrderAddress());
			result = pstmt.executeUpdate();

			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs.next()) {
				orderId = rs.getInt(1); // auto_increment id
			}

		} catch (Exception e) {
			throw new Exception("order_set 업데이트 실패");
		} finally {
			CRUDRepository.closePreparedStatement(pstmt);
			connectionPool.releaseConnection(con);
		}

		// 성공하면 ordersetId
		return orderId;
	}


	
	
	

}
