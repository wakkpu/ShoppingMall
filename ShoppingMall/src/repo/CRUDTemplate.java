package repo;

import resources.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CRUDTemplate<T> {
    ConnectionPool cp;
    public CRUDTemplate(){
        try {
            cp = ConnectionPool.create();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<T> select(String query, RowMapper<T> rowMapper) {
        ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        List<T> itemList = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                T item = rowMapper.mapRow(rset);
                itemList.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException("DB 조회에 실패하였습니다.");
        } finally {
            try {
                CRUDRepository.closeRset(rset);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                CRUDRepository.closePstmt(pstmt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            cp.releaseConnection(con);
        }
        return itemList;
    }

    public T selectOne(String query, RowMapper<T> rowMapper) {
        ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        T result = null;
        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                result = rowMapper.mapRow(rset);
            }
        } catch (Exception e) {
            throw new RuntimeException("DB 조회에 실패하였습니다.");
        } finally {
            try {
                CRUDRepository.closeRset(rset);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                CRUDRepository.closePstmt(pstmt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            cp.releaseConnection(con);
        }
        return result;
    }

    public <T> T selectOneColumn(String query, Class<T> returnType) {
        ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        T result = null;
        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                result = returnType.cast(rset.getObject(1)); // 첫 번째 컬럼의 값을 가져와 캐스팅
            }
        } catch (Exception e) {
            throw new RuntimeException("DB 조회에 실패하였습니다.", e);
        } finally {
            try {
                CRUDRepository.closeRset(rset);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                CRUDRepository.closePstmt(pstmt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            cp.releaseConnection(con);
        }
        return result;
    }

    public int insert(Connection connection,String query){ // 트랜잭션 처리를 위한 connection 파라미터 추가
        int result = 0;
        Connection con = connection;
        if(connection == null){
            cp.getConnection();
        }
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(query);
            result = pstmt.executeUpdate();
        }catch(Exception e){
            throw new RuntimeException();
        }finally {
            try {
                CRUDRepository.closePstmt(pstmt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            cp.releaseConnection(con);
        }
        return result;
    }
}