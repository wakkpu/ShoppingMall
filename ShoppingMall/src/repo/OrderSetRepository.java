package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entity.OrderSet;
import util.ConnectionPool;

public class OrderSetRepository{

	ConnectionPool cp;
	
	public OrderSetRepository() {
		try {
			cp = ConnectionPool.create();
		} catch (SQLException e) {
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
				orderId = rs.getInt(1); // 키값 초기화
			}

		} catch (Exception e) {
			throw new Exception("order_set 업데이트 실패");
		} finally {
			CRUDRepository.closePstmt(pstmt);
			cp.releaseConnection(con);
		}

		// 성공하면 ordersetId
		return orderId;
	}


	
	
	

}
