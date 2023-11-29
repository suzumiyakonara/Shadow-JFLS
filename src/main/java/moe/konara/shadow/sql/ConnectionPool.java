package moe.konara.shadow.sql;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionPool {
    protected BasicDataSource DataSource;
    public ConnectionPool(String url){
        DataSource = new BasicDataSource();
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
    public Statement createStatement() throws SQLException{
        return this.getConnection().createStatement();
    }
}
