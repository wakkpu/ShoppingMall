package repo;

import Entity.Item;
import Entity.Membership;

import java.sql.Connection;

public class MembershipRepository {
    RowMapper membershipRowMapper;
    CRUDTemplate<Item> crudTemplate = new CRUDTemplate<>();

    public MembershipRepository(){
        membershipRowMapper = rset-> Membership.builder()
                .membershipId(rset.getLong("membership_id"))
                .grade(rset.getString("grade"))
                .discountRate(rset.getDouble("discount_rate"))
                .requirement(rset.getInt("requirement"))
                .build();
    }

    public int insertMembership(Connection connection, String query){
        return crudTemplate.insert(query);
    }
}
