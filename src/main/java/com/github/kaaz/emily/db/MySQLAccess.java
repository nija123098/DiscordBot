package com.github.kaaz.emily.db;
/**
 * Created by Soarnir on 16/7/17.
 */

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLAccess {

    private Statement statement = null;
    private ResultSet resultSet = null;

    /*String query = "CREATE TABLE test" +
            "(test_id INT(10) NOT NULL," +
            "test_text VARCHAR(100) NOT NULL," +
            "PRIMARY KEY (test_id))";
    */

    private String DB_NAME;
    private String DB_USER;
    private String DB_ADDRESS;
    private String DB_PASSWORD;
    private Connection c;

    public MySQLAccess(String server, String databaseUser, String databasePassword, String databaseName) {
        DB_ADDRESS = server;
        DB_USER = databaseUser;
        DB_PASSWORD = databasePassword;
        DB_NAME = databaseName;
    }

    private Connection createConnection() {
        try {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setUser(DB_USER);
            dataSource.setPassword(DB_PASSWORD);
            dataSource.setServerName(DB_ADDRESS);
            dataSource.setPort(3306);
            dataSource.setDatabaseName(DB_NAME);
            dataSource.setZeroDateTimeBehavior("convertToNull");
            dataSource.setUseUnicode(true);
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Connection getConnection() {
        if (c == null) {
            c = createConnection();
        }
        return c;
    }

    public ResultSet select(String sql) throws SQLException {
        PreparedStatement query;
        query = getConnection().prepareStatement(sql);
        return query.executeQuery();
    }

    public int query(String sql) throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            return stmt.executeUpdate(sql);
        }
    }

    public int insert(String sql) throws SQLException {
        try (PreparedStatement query = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            query.executeUpdate();
            ResultSet rs = query.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return -1;
    }

    // close errythang
    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (c != null) {
                c.close();
            }
        } catch (Exception e) {

        }
    }

}