package util;

import resources.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 우선 싱글 스레드만 가정
 */
public class TransactionLocal {
    private static ConnectionPool cp;
    private Connection connection = null;

    public static TransactionLocal transactionLocal;

    static{
        transactionLocal = new TransactionLocal();
        try {
            cp = ConnectionPool.create();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private TransactionLocal(){}

    public Connection createConnection(){
        if(connection==null){
            connection = cp.getConnection();
        }
        return connection;
    }

    public void closeConnection(){
        cp.releaseConnection(connection);
        connection = null;
    }
}