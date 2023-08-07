package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Entity.Cargo;
import util.ConnectionPool;

public class CargoRepository {

	ConnectionPool cp;

	public CargoRepository() {
		try {
			cp = ConnectionPool.create();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Long selectItem(Long cargoId) throws Exception {

		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		Cargo cargo = null;

		try {

			pstmt = con.prepareStatement("SELECT * FROM cargo WHERE cargo_id=? Limit 1");

			pstmt.setLong(1, cargoId);

			rset = pstmt.executeQuery();

			if (rset.next()) {

				cargo = Cargo.builder().cargoId(rset.getLong(0)).itemId(rset.getLong(1)).statusId(rset.getLong(2))
						.build();
			}

		} catch (Exception e) {

			throw new Exception("상세 조회 에러 ");

		} finally {
			CRUDRepository.closeRset(rset);
			CRUDRepository.closePstmt(pstmt);
			cp.releaseConnection(con);
		}

		return cargo.getItemId();
	}

	public int updateState(Connection con, long cargoId) throws Exception {

		int result = 0;
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement("UPDATE cargo SET state_id=? WHERE cargo_id=?");

			pstmt.setLong(1, 6L);
			pstmt.setLong(2, cargoId);

			result = pstmt.executeUpdate();
			con.commit();

		} catch (Exception e) {
			con.rollback();
			throw new Exception("상태 업데이트 에러 ");
		}

		return result;
	}

}
