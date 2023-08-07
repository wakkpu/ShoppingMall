package repo;

import Entity.Item;
import dto.CartItemDto;
import resources.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemRepository {
    CRUDTemplate<Item> crudTemplate = new CRUDTemplate<>();
    ConnectionPool cp;
    public ItemRepository(){
        try {
            cp = ConnectionPool.create();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Item> selectAll(){
        RowMapper rowMapper = rset -> Item.builder()
                .itemId(rset.getLong("item_id"))
                .itemName(rset.getString("item_name"))
                .itemPrice(rset.getLong("item_price"))
                .build();
        return crudTemplate.selectAll("select * from item",rowMapper);
        /*ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        List<Item> itemList = new ArrayList<>();
        try {
            pstmt = con.prepareStatement("select * from item");
            rset = pstmt.executeQuery();
            while(rset.next()){
                Item item = Item.builder()
                        .itemId(rset.getLong("item_id"))
                        .itemName(rset.getString("item_name"))
                        .itemPrice(rset.getLong("item_price"))
                        .build();
                itemList.add(item);
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
        return itemList;*/
    }

    public List<Item> selectWithIn(String condition){
        ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        List<Item> itemList = new ArrayList<>();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("select * from item ");
            sb.append(condition);
            pstmt = con.prepareStatement(sb.toString());
            rset = pstmt.executeQuery();
            while(rset.next()){
                Item item = Item.builder()
                        .itemId(rset.getLong("item_id"))
                        .itemName(rset.getString("item_name"))
                        .itemPrice(rset.getLong("item_price"))
                        .build();
                itemList.add(item);
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
        return itemList;
    }
}
