package repo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import Entity.Consumer;
import util.ConnectionPool;

public class ConsumerRepository implements CRUDRepository<Long, Consumer> {
	Logger log = Logger.getLogger("ConsumerRepo");
	ConnectionPool cp;
	long lastId = 1;
	@Override
	public int insert(Consumer v) throws Exception {
		int result = 0;
		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		
		try {
			pstmt = con.prepareStatement(SQL.consumInsert);
			pstmt.setLong(1, lastId++);
			pstmt.setLong(2, );
			pstmt.setString(3, )
		} catch(Exception e) {
			
		}
		
		
		return 0;
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
	
	public Consumer selectByEmail(String useremail) {
		Consumer consumer = null;
		Connection con = cp.getConnection();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		try {
			pstmt = con.prepareStatement(SQL.consumSelect);
			pstmt.setString(1, useremail);
			
			rset = pstmt.executeQuery();
			rset.next();
			consumer = Consumer.builder()
					.userEmail(rset.getString("userEmail"))
					.isAdmin(rset.getString("isAdmin"))
					.build();
		} catch(Exception e) {
			log.info(e.getMessage());
			throw new Exception("조회에러");
		} finally {
			
		}
		return consumer;
	}

}
