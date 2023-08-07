package repo;

import Entity.CartItem;
import dto.CartKey;
import dto.CartItemDto;
import util.ConnectionPool;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CartRepository{
    ConnectionPool cp;
    public CartRepository(){
        try {
            cp = ConnectionPool.create();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(CartItem cartItem){
        int result = 0;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement("insert into cart_item(item_id,consumer_id,item_quantity) values(?,?,?)");
            pstmt.setLong(1, cartItem.getItemId());
            pstmt.setLong(2, cartItem.getConsumerId());
            pstmt.setLong(3, cartItem.getItemQuantity());
            result = pstmt.executeUpdate();
            //con.commit();
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

    public int updateQuantity(CartKey cartKey, int plusNumber){
        int result = 0;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = con.prepareStatement("update cart_item set item_quantity = item_quantity + ? where item_id = ? and consumer_id = ?");
            pstmt.setLong(1,plusNumber);
            pstmt.setLong(2, cartKey.getItemId());
            pstmt.setLong(3, cartKey.getConsumerId());
            result = pstmt.executeUpdate();
            //con.commit();
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

    public int delete(CartKey cartKey){
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        int result = 0;
        try {
            StringBuilder sql = new StringBuilder("DELETE FROM cart_item WHERE consumer_id=? ");
            if(cartKey.getItemId()!=null){
                sql.append("AND item_id = ?");
            }
            pstmt = con.prepareStatement(sql.toString());
            pstmt.setLong(1, cartKey.getConsumerId());
            if(cartKey.getItemId()!=null){
                pstmt.setLong(2, cartKey.getItemId());
            }
            result = pstmt.executeUpdate();
            //con.commit();
        } catch (SQLException e) {
            throw new RuntimeException("카트 아이템 삭제에 실패하였습니다.");
        } finally {
            try {
                CRUDRepository.closePstmt(pstmt);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            cp.releaseConnection(con);
        }
        return result;
    }

    public CartItem selectOne(CartKey cartKey){
        ResultSet rset = null;
        CartItem cartItem = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;

        try {
            pstmt = con.prepareStatement("select * from cart_item where consumer_id = ? and item_id=?");
            pstmt.setLong(1,cartKey.getConsumerId());
            pstmt.setLong(2,cartKey.getItemId());
            rset = pstmt.executeQuery();
            if (rset.next()) { // 결과가 있다면
                System.out.println(rset.getLong("item_id"));
                cartItem = CartItem.builder()
                        .itemId(rset.getLong("item_id"))
                        .consumerId(rset.getLong("consumer_id"))
                        .itemQuantity(rset.getLong("item_quantity"))
                        .build();
            }
        }catch(Exception e){
            throw new RuntimeException("");
        }finally {
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
        return cartItem;
    }


    public List<CartItemDto> selectCart(){
        ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        List<CartItemDto> custList = new ArrayList<>();
        try {
            pstmt = con.prepareStatement("select a.item_id,item_name,item_price,item_quantity from cart_item a left join item b on a.item_id=b.item_id where consumer_id=?");
            pstmt.setInt(1, 1); // 이 부분 객체의 값으로 수정 필요
            rset = pstmt.executeQuery();
            while(rset.next()){
                CartItemDto cartItemDto = CartItemDto.builder()
                        .itemId(rset.getLong("item_id"))
                        .itemName(rset.getString("item_name"))
                        .itemPrice(rset.getLong("item_price"))
                        .itemQuantity(rset.getLong("item_quantity"))
                        .build();
                custList.add(cartItemDto);
            }
        }catch(Exception e){
            throw new RuntimeException("DB 조회에 실패 하였습니다.");
        }finally {
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
        return custList;
    }
}