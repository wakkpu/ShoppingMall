package repo;

import Entity.Consumer;
import Entity.Membership;
import resources.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ConsumerRepository {
	Logger log = Logger.getLogger("ConsumerRepo");

	RowMapper consumerRowMapper;
	RowMapper membershipRowMapper;

	CRUDTemplate<Consumer> consumerCRUDTemplate = new CRUDTemplate<>();
	CRUDTemplate<Membership> membershipCRUDTemplate = new CRUDTemplate<>();

	ConnectionPool cp;
	
	public ConsumerRepository()  {
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
		Object[] obj = {consumer.getUserEmail(), consumer.getPassword(), consumer.getPhoneNumber(),
						consumer.getAddress(), consumer.getUserName(), consumer.isAdmin(), consumer.getMembershipId()};
		return consumerCRUDTemplate.insert(SQL.consumInsert,obj);
	}

	public Consumer selectByEmail(String userEmail) {
		String sql = "SELECT * FROM consumer WHERE user_email = \"" + userEmail +"\"";
		return consumerCRUDTemplate.selectOne(sql, consumerRowMapper);
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
			cp.releaseConnection(con);
		}
		return membership;
	}
}
