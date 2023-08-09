package repo;

import util.TransactionLocal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CRUDTemplate<T> {
    public int insert(String query, Object ... params){
        int result;
        /**
         * 커넥션 관련 관리 생각해야 할 듯?
         */
        Connection con = TransactionLocal.transactionLocal.createConnection();

        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement(query);
            for (int i = 0; i < params.length; i++) { // 파라미터가 존재하는 경우
                pstmt.setObject(i + 1, params[i]); // 알맞은 타입으로 바인딩
            }
            result = pstmt.executeUpdate();
        }catch(Exception e){
            throw new RuntimeException(e);
        }finally {
            closeResources(null, pstmt, con);
        }
        return result;
    }

    public List<T> select(String query, RowMapper<T> rowMapper, Object... params) {
        ResultSet rset = null;
        Connection con = TransactionLocal.transactionLocal.createConnection();
        PreparedStatement pstmt = null;
        List<T> itemList = new ArrayList<>();
        try {
            pstmt = con.prepareStatement(query);

            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            rset = pstmt.executeQuery();
            while (rset.next()) {
                T item = rowMapper.mapRow(rset);
                itemList.add(item);
            }
        } catch (Exception e) {
            throw new RuntimeException("DB 조회에 실패하였습니다.",e);
        } finally {
            closeResources(rset, pstmt, con);
        }
        return itemList;
    }


    public T selectOne(String query, RowMapper<T> rowMapper, Object... params) {
        ResultSet rset = null;
        Connection con = TransactionLocal.transactionLocal.createConnection();
        PreparedStatement pstmt = null;
        T result = null;
        try {
            pstmt = con.prepareStatement(query);
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
            rset = pstmt.executeQuery();
            if (rset.next()) {
                result = rowMapper.mapRow(rset);
            }
        } catch (Exception e) {
            throw new RuntimeException("DB 조회에 실패하였습니다.",e);
        } finally {
            closeResources(rset, pstmt, con);
        }
        return result;
    }

    /**
        해당 메소드는 하나의 결과 값이 필요한 경우 사용되는 메소드
        query : 실행될 실제 쿼리( select MAX(item_id) from item )
        returnType : query를 실행하고 얻기를 희망하는 타입 위의 경우 Long임

        fetchSingleValue("select MAX(item_id) from item", Long.class)
     */
    public <T> T fetchSingleValue(String query, Class<T> returnType) {
        ResultSet rset = null;
        Connection con = TransactionLocal.transactionLocal.createConnection();
        PreparedStatement pstmt = null;
        T result = null;
        try {
            pstmt = con.prepareStatement(query);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                result = returnType.cast(rset.getObject(1));
            }
        } catch (Exception e) {
            throw new RuntimeException("DB 조회에 실패하였습니다.", e);
        } finally {
            closeResources(rset, pstmt, con);
        }
        return result;
    }

    private void closeResources(ResultSet rset, PreparedStatement pstmt, Connection con) {
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
        //TransactionLocal.transactionLocal.closeConnection();
        //cp.releaseConnection(con);
    }
}