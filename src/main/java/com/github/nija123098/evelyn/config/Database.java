package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.CallBuffer;
import com.github.nija123098.evelyn.util.Log;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Controls interaction with the database.
 *
 * @author Soarnir
 * @author nija123098
 * @since 1.0.0
 */
public class Database {
    private static final CallBuffer CALL_BUFFER = new CallBuffer(250);
    private static final Connection CONNECTION;
    private static final QueryRunner RUNNER;
    static {
        String username = ConfigProvider.DATABASE_SETTINGS.username();
        String password = ConfigProvider.DATABASE_SETTINGS.password();
        String serverIP = ConfigProvider.DATABASE_SETTINGS.ipAddress();
        Integer serverPort = ConfigProvider.DATABASE_SETTINGS.port();
        String dbName = ConfigProvider.DATABASE_SETTINGS.dbName();
        Connection c = null;
        try {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setUser(username);
            dataSource.setPassword(password);
            dataSource.setServerName(serverIP);
            dataSource.setPort(serverPort);
            dataSource.setDatabaseName(dbName);
            dataSource.setZeroDateTimeBehavior("convertToNull");
            dataSource.setUseUnicode(true);
            c = dataSource.getConnection();
        } catch (SQLException e) {
            Log.log("Could not init database connection", e);
        }
        CONNECTION = c;
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(20);
        config.setJdbcUrl("jdbc:mariadb://" + serverIP + ":" + serverPort + "/" + dbName);
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        RUNNER = new QueryRunner(new HikariDataSource(config));
        query("SET NAMES utf8mb4");
    }

    public static void init() {
    }

    public static void bufferCall(Runnable runnable){
        CALL_BUFFER.call(runnable);
    }

    public static Connection getConnection() {
        return CONNECTION;
    }

    public static <E> E select(String sql, ResultSetHandler<E> handler) {
        try{return RUNNER.query(sql, handler);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not select: ERROR: " + e.getErrorCode() + " " + sql, e);
        }
    }

    public static void query(String sql) {
        try{RUNNER.update(sql);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not query: ERROR: " + e.getErrorCode() + " " + sql, e);
        }
    }

    public static void insert(String sql) {
        try{RUNNER.update(sql);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not insert: ERROR: " + e.getErrorCode() + " " + sql, e);
        }
    }
    public static String quote(String id){
        return Character.isDigit(id.charAt(0)) ? id : "'" + id + "'";
    }
}