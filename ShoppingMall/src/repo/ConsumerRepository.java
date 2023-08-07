package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import Entity.Consumer;
import Entity.Membership;
import dto.LoginResultDto;
import resources.ConnectionPool;

public class ConsumerRepository implements CRUDRepository<Long, Consumer> {
	Logger log = Logger.getLogger("ConsumerRepo");
	ConnectionPool cp;
	
	public ConsumerRepository() {
		try {
			cp = ConnectionPool.create();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int insert(Consumer v) throws Exception {
		int result = 0;
		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(SQL.consumInsert);
			pstmt.setLong(1, v.getConsumerId());
			pstmt.setLong(2, v.getMembershipId());
			pstmt.setString(3, v.getUserEmail());
			pstmt.setString(4, v.getPassword());
			pstmt.setString(5, v.getPhoneNumber());
			pstmt.setString(6, v.getAddress());
			pstmt.setString(7, v.getUserName());
			pstmt.setInt(8, (v.isAdmin()) ? 1 : 0);
			
			result = pstmt.executeUpdate();
			log.info("회원가입 완료");
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new Exception("회원가입 에러");
		} finally {
			closePstmt(pstmt);
			cp.releaseConnection(con);
		}
		
		return result;
	}

	@Override
	public int update(Consumer v) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(Long k) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Optional<Consumer> select(Long k) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Consumer> selectAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Consumer selectByEmail(String userEmail) throws Exception {
		Consumer found = null;
		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			pstmt = con.prepareStatement(SQL.consumSelect);
			pstmt.setString(1, userEmail);
			
			rset = pstmt.executeQuery();
			rset.next();
			found = Consumer.builder()
					.consumerId(rset.getLong("consumer_id"))
					.userEmail(userEmail)
					.password(rset.getString("password"))
					.userName(rset.getString("user_name"))
					.build();
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new Exception("조회에러");
		} finally {
			closeRset(rset);
			closePstmt(pstmt);
			cp.releaseConnection(con);
		}
		return found;
	}
	
	public String selectMembershipById(Long consumerId) throws Exception {
		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		String grade = null;
		try {
			pstmt = con.prepareStatement(SQL.membershipSelect);
			pstmt.setLong(1, consumerId);
			
			rset = pstmt.executeQuery();
			rset.next();
			grade = rset.getString("grade");
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new Exception("조회에러");
		} finally {
			closeRset(rset);
			closePstmt(pstmt);
			cp.releaseConnection(con);
		}
		return grade;
	}
	
	public void closePstmt(PreparedStatement pstmt) throws Exception {
		if(pstmt != null) {
			pstmt.close();
		}
	}
	
	public void closeRset(ResultSet rset) throws Exception {
		if(rset != null) {
			rset.close();
		}
	}

}
