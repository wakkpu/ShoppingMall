package test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import util.TransactionLocal;

import java.sql.Connection;
import java.sql.SQLException;

public class RootTest {
    Connection connection;

    @BeforeEach
    void before() throws SQLException {
        connection = TransactionLocal.transactionLocal.createConnection();
        connection.setAutoCommit(false);
        beforeHook();
    }

    @AfterEach
    void after() throws SQLException {
        connection.rollback();
        connection.setAutoCommit(true); // 이거 해줘야함 (커넥션 풀을 사용하므로)
        TransactionLocal.transactionLocal.closeConnection();
        afterHook();
    }

    public void beforeHook(){}
    public void afterHook(){}
}
