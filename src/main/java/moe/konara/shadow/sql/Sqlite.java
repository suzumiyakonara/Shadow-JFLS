package moe.konara.shadow.sql;

import org.slf4j.Logger;

import java.sql.*;
import java.util.Collection;
import java.util.Iterator;

public class Sqlite {
    DatabaseMetaData Meta;
    Logger Logger;
    String Url;
    ConnectionPool Pool;
    public Sqlite(String file_path, Logger logger) {
        this.Logger = logger;
        this.Url = "jdbc:sqlite:" + file_path;
        this.Pool = new ConnectionPool(this.Url);
        try {
            Connection conn = this.Pool.getConnection();
            if (conn != null) {
                Meta = conn.getMetaData();
                this.Logger.info(Meta.getDriverName() + " database has been connected.");
            }else throw new SQLException();
        } catch (SQLException e) {
            this.Logger.error(e.getMessage());
            this.Logger.error("Throw error when creating SQL.");
        }
    }

    public void createNewTableIfNotExists(String table_name, Collection<ColumnSetting> column_settings) {
        try {
            if(table_name.isEmpty() || column_settings.isEmpty())
                throw new SQLException();
            StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF NOT EXISTS ")
                    .append(table_name).append(" ")
                    .append("(");
            Iterator<ColumnSetting> setting_iterator = column_settings.iterator();
            while (setting_iterator.hasNext()) {
                sql.append(setting_iterator.next().toString());
                if(setting_iterator.hasNext()) sql.append(",");
            }
            sql.append(");");
            Connection conn = Pool.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(sql.toString());
            stmt.close();
            conn.close();
            Logger.info("Created table " + table_name);
        } catch (SQLException e) {
            this.Logger.error(e.getMessage());
            this.Logger.error("Throw error when creating new table.");
        }
    }

    public void insertData(String table_name, Collection<ColumnValue> column_values){
        try {
            if(table_name.isEmpty() || column_values.isEmpty())
                throw new SQLException();
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO ").append(table_name).append(" ");
            StringBuilder columns = new StringBuilder().append("(");
            StringBuilder values = new StringBuilder().append("(");
            Iterator<ColumnValue> column_value_iterator = column_values.iterator();
            while (column_value_iterator.hasNext()){
                ColumnValue tmp = column_value_iterator.next();
                columns.append(tmp.getColumn());
                if(tmp.getValue() instanceof String) values.append("\"");
                values.append(tmp.getValue());
                if(tmp.getValue() instanceof String) values.append("\"");
                if(column_value_iterator.hasNext()){
                    columns.append(",");
                    values.append(",");
                }
            }
            columns.append(")");
            values.append(")");
            sql.append(columns).append(" VALUES").append(values).append(";");
            Connection conn = Pool.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(sql.toString());
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            this.Logger.error(e.getMessage());
            this.Logger.error("Throw error when insert data.");
        }
    }

}
