package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import Entity.Cargo;
import dto.CargoDto;
import resources.ConnectionPool;

public class CargoRepository implements CRUDRepository<Long, Cargo> {
	
	ConnectionPool connectionPool;
	
	Logger logger = Logger.getLogger("Cargo Repository");
	
	public CargoRepository() {
		try {
			connectionPool = ConnectionPool.create();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int insert(Cargo v) throws Exception {
		
		int result = 0;
		Connection conn = connectionPool.getConnection();
		PreparedStatement pStmt = null;
		
		final String insertCargo = "INSERT INTO cargo(item_id, status_id) VALUES(?, ?)";
		
		try {
			pStmt = conn.prepareStatement(insertCargo);
			pStmt.setLong(1, v.getItemId());
			pStmt.setLong(2, v.getStatusId());
			
			result = pStmt.executeUpdate();
		} catch(Exception e) {
			logger.info(e.getMessage());
			throw e;
		} finally {
			CRUDRepository.closePreparedStatement(pStmt);
			connectionPool.releaseConnection(conn);
		}
		
		return result;
	}

	@Override
	public int update(Cargo v) throws Exception {
		int result = 0;
		Connection conn = connectionPool.getConnection();
		PreparedStatement pStmt = null;
		
		final String updateCargo = "UPDATE cargo SET status_id = ? WHERE cargo_id = ?";
		
		try {
			pStmt = conn.prepareStatement(updateCargo);
			pStmt.setLong(1, v.getStatusId());
			pStmt.setLong(2, v.getCargoId());
			
			result = pStmt.executeUpdate();
		} catch(Exception e) {
			logger.info(e.getMessage());
			throw e;
		} finally {
			CRUDRepository.closePreparedStatement(pStmt);
			connectionPool.releaseConnection(conn);
		}
		return result;
	}

	@Override
	public int delete(Long k) throws Exception {
		int result = 0;
		
		return result;
	}

	@Override
	public Optional<Cargo> select(Long k) throws Exception {
		return null;
	}

	@Override
	public List<Cargo> selectAll() throws Exception {
		return null;
	}
	
	public List<CargoDto> selectAllCargo() throws Exception {
		List<CargoDto> result = new ArrayList<>();
		
		Connection conn = connectionPool.getConnection();
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		
		final String selectAllCargo = 
				"SELECT cargo_id, item_name, status_name FROM cargo c " + 
				"JOIN item i ON i.item_id = c.item_id " + 
				"JOIN status s ON s.status_id = c.status_id";
		try {
			pStmt = conn.prepareStatement(selectAllCargo);
			resultSet = pStmt.executeQuery();
			
			while(resultSet.next()) {
				result.add(
						CargoDto.builder()
						.cargoId(resultSet.getLong("cargo_id"))
						.itemName(resultSet.getString("item_name"))
						.statusName(resultSet.getString("status_name"))
						.build()
						);
			}
		} catch(Exception e) {
			logger.info(e.getMessage());
			throw e;
		} finally {
			CRUDRepository.closePreparedStatement(pStmt);
			connectionPool.releaseConnection(conn);
		}
		
		return result;
	}
	
	public List<CargoDto> selectAllCargoCount() throws Exception {
		List<CargoDto> result = new ArrayList<>();

		Connection conn = connectionPool.getConnection();
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		
		final String getAllCount =
				"SELECT item_name, COUNT(item_name) as cargo_count FROM cargo c " + 
				"JOIN item i ON i.item_id = c.item_id " +
				"GROUP BY i.item_name";
		try {
			pStmt = conn.prepareStatement(getAllCount);
			resultSet = pStmt.executeQuery();
			
			while(resultSet.next()) {
				result.add(CargoDto.builder()
						.itemName(resultSet.getString("item_name"))
						.cargoCount(resultSet.getLong("cargo_count"))
						.build()
						);
			}
		} catch(Exception e) {
			logger.info(e.getMessage());
			throw e;
		} finally {
			CRUDRepository.closePreparedStatement(pStmt);
			connectionPool.releaseConnection(conn);
		}
		
		return result;
	}

}
