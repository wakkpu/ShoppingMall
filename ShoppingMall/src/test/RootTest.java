package test;

import Entity.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import repo.CRUDTemplate;
import resources.ConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class RootTest {
    Connection connection;
    ConnectionPool connectionPool;
    CRUDTemplate<Item> crudTemplate = new CRUDTemplate<>();

    @BeforeEach
    void before() throws SQLException {
        connectionPool = ConnectionPool.create();
        connection = connectionPool.getConnection();
        connection.setAutoCommit(false);
    }

    @AfterEach
    void after() throws SQLException {
        connection.rollback();
        connectionPool.releaseConnection(connection);
    }
}
