package repo;

import Entity.Category;
import dto.CategoryDataDto;
import dto.CategoryDto;
import resources.ConnectionPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {
    ConnectionPool cp;
    public CategoryRepository(){
        try {
            cp = ConnectionPool.create();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Category> selectAll(){
        ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        List<Category> categoryList = new ArrayList<>();
        try {
            pstmt = con.prepareStatement("select * from category where master_category_id is null ");

            rset = pstmt.executeQuery();
            while(rset.next()){
                Category cartItemDto = Category.builder()
                        .categoryId(rset.getLong("category_id"))
                        .categoryName(rset.getString("category_name"))
                        .masterCategoryId(rset.getLong("master_category_id"))
                        .build();
                categoryList.add(cartItemDto);
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
        return categoryList;
    }

    public CategoryDataDto selectWithCategoryId(Long categoryId){
        ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        CategoryDataDto categoryDataDto = null;
        try {
            pstmt = con.prepareStatement("SELECT child.category_id AS category_id1, grandChild.category_id AS category_id2,parent.category_id AS category_id3 FROM category parent LEFT JOIN category child ON parent.category_id = child.master_category_id LEFT JOIN category grandChild ON child.category_id = grandChild.master_category_id WHERE parent.category_id = ?");
            pstmt.setLong(1,categoryId );
            rset = pstmt.executeQuery();
            if (rset.next()) { // 결과가 있다면
                categoryDataDto = CategoryDataDto.builder()
                        .categoryId1(rset.getLong("category_id1"))
                        .categoryId2(rset.getLong("category_id2"))
                        .categoryId3(rset.getLong("category_id3"))
                        .build();
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

        return categoryDataDto;
    }

    public List<CategoryDto> getChildCategory(Long categoryId){
        ResultSet rset = null;
        Connection con = cp.getConnection();
        PreparedStatement pstmt = null;
        List<CategoryDto> result = new ArrayList<>();
        try {
            pstmt = con.prepareStatement("SELECT child.category_id, child.category_name FROM category parent LEFT JOIN category child ON parent.category_id = child.master_category_id WHERE parent.category_id = ?");
            pstmt.setLong(1,categoryId );
            rset = pstmt.executeQuery();
            while (rset.next()) { // 결과가 있다면
                CategoryDto categoryDto = CategoryDto.builder()
                        .categoryId(rset.getLong("category_id"))
                        .categoryName(rset.getString("category_name"))
                        .build();
                result.add(categoryDto);
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

        return result;
    }
}