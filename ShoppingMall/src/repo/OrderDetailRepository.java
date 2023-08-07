package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import Entity.Item;
import Entity.OrderDetail;
import util.ConnectionPool;

public class OrderDetailRepository {

	ConnectionPool cp;

	public OrderDetailRepository() {
		try {
			cp = ConnectionPool.create();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int insertOrder(Connection con, List<OrderDetail> orderDetailList) throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		pstmt = con.prepareStatement("insert into order_detail values(?, ?, ?, ?)");
		try {
			for (OrderDetail orderDetail : orderDetailList) {

				pstmt.setLong(1, orderDetail.getOrderSetId());
				pstmt.setLong(2, orderDetail.getCargoId());
				pstmt.setLong(3, orderDetail.getStatusId());
				pstmt.setLong(4, orderDetail.getBuyPrice());
				result = pstmt.executeUpdate();
			}

		} catch (Exception e) {
			throw new Exception("주문 디테일 정보 삽입 에러 ");
		}

		return result;
	}

	public int updateOrder(Connection con, List<OrderDetail> orderDetailList) throws Exception {
		int result = 0;
		PreparedStatement pstmt = null;
		try {
			pstmt = con.prepareStatement("UPDATE order_detail SET status_id=? WHERE cargo_id=?");

			for (OrderDetail orderDetail : orderDetailList) {

				pstmt.setLong(1, orderDetail.getOrderSetId());
				pstmt.setLong(2, orderDetail.getCargoId());
				pstmt.setLong(3, orderDetail.getStatusId());
				pstmt.setLong(4, orderDetail.getBuyPrice());
				result = pstmt.executeUpdate();
			}
		} catch (Exception e) {
			throw new Exception("주문 디테일 정보 조회 에러 ");
		}

		return result;
	}

	public long selectPrice(Long cargoId) throws Exception {

		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		long price = 0;

		try {
			pstmt = con.prepareStatement("SELECT price FROM item WHERE cargo_id=?");

			pstmt.setLong(1, cargoId);

			rset = pstmt.executeQuery();

			if (rset.next()) {
				price = rset.getLong(1);
			}
		} catch (Exception e) {
			throw new Exception("가격 정보 에러 ");
		} finally {
			CRUDRepository.closeRset(rset);
			CRUDRepository.closePstmt(pstmt);
			cp.releaseConnection(con);
		}

		return price;
	}

}
