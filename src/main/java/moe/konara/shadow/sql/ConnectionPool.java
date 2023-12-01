package moe.konara.shadow.sql;

import org.apache.commons.dbcp2.BasicDataSource;
import org.sqlite.JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {
    protected BasicDataSource DataSource;
    public ConnectionPool(String url) throws ClassNotFoundException, SQLException {
        DataSource = new BasicDataSource();
        Class.forName("org.sqlite.JDBC");
        DriverManager.registerDriver(new JDBC());
        DataSource.setUrl(url);
        DataSource.setMinIdle(5);
        DataSource.setMaxIdle(10);
        DataSource.setMaxTotal(25);
    }
    public Connection getConnection() throws SQLException{
        return this.DataSource.getConnection();
    }

    public BasicDataSource getDataSource() {
        return DataSource;
    }
}
