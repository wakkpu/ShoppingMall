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
	
	public int insert(Connection connection, Consumer v){
		int result = 0;
		Connection con = connection;
		if(con==null){
			con = cp.getConnection();
		}
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
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new RuntimeException("회원가입 에러");
		} finally {
			try {
				closePstmt(pstmt);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			cp.releaseConnection(con);
		}
		
		return result;
	}

	@Override
	public int insert(Consumer consumer) throws Exception {
		return 0;
	}

	@Override
	public int update(Consumer v) throws Exception {
		int result = 0;
		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(SQL.consumUpdate);
			pstmt.setString(1, v.getUserName());
			pstmt.setString(2, v.getPhoneNumber());
			pstmt.setString(3, v.getAddress());
			pstmt.setString(4, v.getUserEmail());
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new Exception("회원 정보 수정 에러");
		} finally {
			closePstmt(pstmt);
			cp.releaseConnection(con);
		}
		
		return result;
	}

	@Override
	public int delete(Long k){
		int result = 0;
		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(SQL.consumDelete);
			pstmt.setLong(1, k);
			
			result = pstmt.executeUpdate();
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new RuntimeException("회원 삭제 에러");
		} finally {
			try {
				closePstmt(pstmt);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			cp.releaseConnection(con);
		}
		
		return result;
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
	
	public Consumer selectByEmail(String userEmail){
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
					.phoneNumber(rset.getString("phone_number"))
					.address(rset.getString("address"))
					.password(rset.getString("password"))
					.userName(rset.getString("user_name"))
					.build();
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new RuntimeException("consumer 조회 에러");
		} finally {
			try {
				closeRset(rset);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			try {
				closePstmt(pstmt);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			cp.releaseConnection(con);
		}
		return found;
	}
	
	public Membership selectMembershipById(Long consumerId) throws Exception {
		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		Membership membership = null;
		try {
			pstmt = con.prepareStatement(SQL.membershipSelect);
			pstmt.setLong(1, consumerId);
			
			rset = pstmt.executeQuery();
			rset.next();
			membership = Membership.builder()
							.grade(rset.getString("grade"))
							.discountRate(rset.getDouble("discount_rate"))
							.build();
//			grade = rset.getString("grade");
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new Exception("멤버십 조회 에러");
		} finally {
			closeRset(rset);
			closePstmt(pstmt);
			cp.releaseConnection(con);
		}
		return membership;
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
