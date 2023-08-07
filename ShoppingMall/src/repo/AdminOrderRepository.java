package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import Entity.OrderSet;
import resources.ConnectionPool;

public class AdminOrderRepository implements CRUDRepository<Long, OrderSet> {

	ConnectionPool connectionPool;
	
	public AdminOrderRepository() {
		try {
			connectionPool = ConnectionPool.create();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int insert(OrderSet v) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(OrderSet v) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Long k) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Optional<OrderSet> select(Long k) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OrderSet> selectAll() throws Exception {
		List<OrderSet> result = new ArrayList<>();
		
		Connection conn = connectionPool.getConnection();
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		
		final String selectOrderSetsByStatusId = 
				"SELECT * FROM order_set ";
		try {
			pStmt = conn.prepareStatement(selectOrderSetsByStatusId);
			
			resultSet = pStmt.executeQuery();
			
			while(resultSet.next()) {
				result.add(
						OrderSet.builder()
						.orderSetId(resultSet.getLong("order_set_id"))
						.consumerId(resultSet.getLong("consumer_id"))
						.orderCode(resultSet.getString("order_code"))
//						.orderTime(resultSet.getTime("order_time"))
						.orderAddress(resultSet.getString("order_address"))
						.build()
						);
			}
		} catch(Exception e) {
			throw e;
		} finally {
			CRUDRepository.closePreparedStatement(pStmt);
			connectionPool.releaseConnection(conn);
		}
		
		return result;
	}
	
	public List<OrderSet> selectOrderSetsByStatusId(Long statusId) throws Exception {
		List<OrderSet> result = new ArrayList<>();
		
		Connection conn = connectionPool.getConnection();
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		
		final String selectOrderSetsByStatusId = 
				"SELECT order_set_id, consumer_id, order_code, order_time, order_address " +
				"FROM status s " +
				"JOIN order_set os ON os.order_set_id = od.order_set_id " +
				"JOIN order_detail od ON s.status_id = od.status_id and s.status_id = ?";
		
		try {
			pStmt = conn.prepareStatement(selectOrderSetsByStatusId);
			
			resultSet = pStmt.executeQuery();
			
			// DateTimeFormatter
			while(resultSet.next()) {
				result.add(
						OrderSet.builder()
						.orderSetId(resultSet.getLong("order_set_id"))
						.consumerId(resultSet.getLong("consumer_id"))
						.orderCode(resultSet.getString("order_code"))
//						.orderTime(resultSet.getTime("order_time"))
						.orderAddress(resultSet.getString("order_address"))
						.build()
						);
			}
		} catch(Exception e) {
			throw e;
		} finally {
			CRUDRepository.closePreparedStatement(pStmt);
			connectionPool.releaseConnection(conn);
		}
		
		return result;
	}

}
