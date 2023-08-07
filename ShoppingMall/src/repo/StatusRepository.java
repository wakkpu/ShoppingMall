package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import Entity.Status;
import resources.ConnectionPool;

public class StatusRepository implements CRUDRepository<Long, Status>{
	
	ConnectionPool connectionPool;
	
	Logger logger = Logger.getLogger("Status Repository");
	
	public StatusRepository() {
		try {
			connectionPool = ConnectionPool.create();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int insert(Status v) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(Status v) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Long k) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Optional<Status> select(Long k) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Long selectStatusIdByName(String statusName) throws Exception {
		Long result = 0L;
		
		Connection conn = connectionPool.getConnection();
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		
		final String selectStatusByName = 
				"SELECT status_id FROM status WHERE status_name = ?";
		
		try {
			pStmt = conn.prepareStatement(selectStatusByName);
			pStmt.setString(1, statusName);
			
			resultSet = pStmt.executeQuery();
			resultSet.next();
			result = resultSet.getLong("status_id");
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			CRUDRepository.closePreparedStatement(pStmt);
			CRUDRepository.closeResultSet(resultSet);
			connectionPool.releaseConnection(conn);
		}
		
		return result;
	}

	@Override
	public List<Status> selectAll() throws Exception {
		List<Status> result = new ArrayList<Status>();
		
		Connection conn = connectionPool.getConnection();
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		
		final String selectAllStatus = "SELECT * FROM Status";
		
		try {
			pStmt = conn.prepareStatement(selectAllStatus);
			
			resultSet = pStmt.executeQuery();
			
			while(resultSet.next()) {
				result.add(
						Status.builder()
						.statusId(resultSet.getLong("status_id"))
						.statusName(resultSet.getString("status_name"))
						.build()
						);
			}
		} catch(Exception e) {
			logger.info(e.getMessage());
			throw e;
		} finally {
			CRUDRepository.closePreparedStatement(pStmt);
			CRUDRepository.closeResultSet(resultSet);
			connectionPool.releaseConnection(conn);
		}
		
		return result;
	}
}
