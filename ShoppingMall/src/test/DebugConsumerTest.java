package test;

import Entity.Consumer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import repo.ConsumerRepository;
import resources.ConnectionPool;

import java.sql.SQLException;

public class DebugConsumerTest extends RootTest {

    ConsumerRepository consumerRepository = new ConsumerRepository();

    @BeforeEach
    void before() throws SQLException {
        connectionPool = ConnectionPool.create();
        connection = connectionPool.getConnection();
        System.out.println(connection);
        connection.setAutoCommit(false);
    }

    @AfterEach
    void after() throws SQLException {
        connection.rollback();
        connectionPool.releaseConnection(connection);
    }

    @Test
    public void insertTest() {

        Consumer con = Consumer.builder()
                .userEmail("test1@test.com")
                .password("1234")
                .isAdmin(false)
                .phoneNumber("1234")
                .address("1234")
                .userName("userTest")
                .membershipId(1L)
                .build();

//        System.out.println(connection.toString());

        consumerRepository.insert(connection, con);

        Consumer select = consumerRepository.selectByEmail("test1@test.com");

        Assert.assertEquals(con.getAddress(), select.getAddress());
    }
}
