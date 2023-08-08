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

public class ConsumerRepository {
	Logger log = Logger.getLogger("ConsumerRepo");

	RowMapper consumerRowMapper;
	RowMapper membershipRowMapper;

	ConnectionPool cp;


	CRUDTemplate<Consumer> consumerCRUDTemplate = new CRUDTemplate<>();
	CRUDTemplate<Membership> membershipCRUDTemplate = new CRUDTemplate<>();
	
	public ConsumerRepository()  {
		try {
			cp = ConnectionPool.create();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		consumerRowMapper = new RowMapper() {
			@Override
			public Object mapRow(ResultSet rset) throws SQLException {
				return Consumer.builder()
						.consumerId(rset.getLong("consumer_id"))
						.membershipId(rset.getLong("membership_id"))
						.userEmail(rset.getString("user_email"))
						.password(rset.getString("password"))
						.phoneNumber(rset.getString("phone_number"))
						.address(rset.getString("address"))
						.userName(rset.getString("user_name"))
						.isAdmin(rset.getBoolean("is_admin"))
						.build();
			}
		};

		membershipRowMapper = new RowMapper() {
			@Override
			public Object mapRow(ResultSet rset) throws SQLException {
				return Membership.builder()
						.grade(rset.getString("grade"))
						.discountRate(rset.getDouble("discount_rate"))
						.build();
			}
		};

	}

	public int insert(Connection connection,Consumer consumer) {
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO consumer(user_email, password, phone_number, address, user_name, is_admin, membership_id) ")
				.append(" values(\"")
				.append(consumer.getUserEmail())
				.append("\", \"")
				.append(consumer.getPassword())
				.append("\", \"")
				.append(consumer.getPhoneNumber())
				.append("\", \"")
				.append(consumer.getAddress())
				.append("\", \"")
				.append(consumer.getUserName())
				.append("\", ")
				.append(consumer.isAdmin())
				.append(", ")
				.append(consumer.getMembershipId())
				.append(")");

		return consumerCRUDTemplate.insert(connection, sql.toString());
//	}
//	public int insert(Connection connection, Consumer v){
//		int result = 0;
//		Connection con = connection;
//		if(con==null){
//			con = cp.getConnection();
//		}
//		PreparedStatement pstmt = null;
//
//		try {
//			pstmt = con.prepareStatement(SQL.consumInsert);
//			pstmt.setLong(1, v.getMembershipId());
//			pstmt.setString(2, v.getUserEmail());
//			pstmt.setString(3, v.getPassword());
//			pstmt.setString(4, v.getPhoneNumber());
//			pstmt.setString(5, v.getAddress());
//			pstmt.setString(6, v.getUserName());
//			pstmt.setInt(7, (v.isAdmin()) ? 1 : 0);
//
//			result = pstmt.executeUpdate();
//		} catch(Exception e) {
//			log.info(e.getMessage());
//			throw new RuntimeException("회원가입 에러");
//		} finally {
//			try {
//				CRUDRepository.closePstmt(pstmt);
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//			cp.releaseConnection(con);
//		}
//
//		return result;
	}

	public Consumer selectByEmail(String userEmail) {
		String sql = "SELECT * FROM consumer WHERE user_email = \"" + userEmail +"\"";
		System.out.println(sql);
		return consumerCRUDTemplate.selectOne(sql, consumerRowMapper);
	}

//	public Consumer selectByEmail(String userEmail){
//		Consumer found = null;
//		Connection con = cp.getConnection();
//		PreparedStatement pstmt = null;
//		ResultSet rset = null;
//		try {
//			pstmt = con.prepareStatement(SQL.consumSelect);
//			pstmt.setString(1, userEmail);
//
//			rset = pstmt.executeQuery();
//			rset.next();
//			found = Consumer.builder()
//					.consumerId(rset.getLong("consumer_id"))
//					.userEmail(userEmail)
//					.phoneNumber(rset.getString("phone_number"))
//					.address(rset.getString("address"))
//					.password(rset.getString("password"))
//					.userName(rset.getString("user_name"))
//					.build();
//		} catch(Exception e) {
//			log.info(e.getMessage());
//			throw new RuntimeException("consumer 조회 에러");
//		} finally {
//			try {
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//			try {
//			} catch (Exception e) {
//				throw new RuntimeException(e);
//			}
//			cp.releaseConnection(con);
//		}
//		return found;
//	}

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
			cp.releaseConnection(con);
		}
		return membership;
	}
}
