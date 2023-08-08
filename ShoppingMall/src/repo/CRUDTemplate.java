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
}
