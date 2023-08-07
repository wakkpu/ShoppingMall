package repo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

public interface CRUDRepository<K, V> {
	public int insert(V v) throws Exception;
	
	public int update(V v) throws Exception;
	
	public int delete(K k) throws Exception;
	
	public Optional<V> select(K k) throws Exception;
	
	public List<V> selectAll() throws Exception;
	
	public static void closePstmt(PreparedStatement pstmt) throws Exception {
		if(pstmt != null) {
			pstmt.close();
		}
		
	}
	public static void closeRset(ResultSet rset) throws Exception {
		if(rset != null) {
			rset.close();
		}
		
	}
	
}
