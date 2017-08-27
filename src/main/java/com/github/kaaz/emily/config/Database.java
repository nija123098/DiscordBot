package com.github.kaaz.emily.config;

import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.CallBuffer;
import com.github.kaaz.emily.util.Log;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.*;

/**
 * Created by Soarnir on 16/7/17.
 */
public class Database {
    private static final CallBuffer CALL_BUFFER = new CallBuffer(25);
    private static final Connection CONNECTION;
    private static final QueryRunner RUNNER;
    static {
        Connection c = null;
        try {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            dataSource.setUser(BotConfig.DB_USER);
            dataSource.setPassword(BotConfig.DB_PASS);
            dataSource.setServerName(BotConfig.DB_HOST);
            dataSource.setPort(3306);
            dataSource.setDatabaseName(BotConfig.DB_NAME);
            dataSource.setZeroDateTimeBehavior("convertToNull");
            dataSource.setUseUnicode(true);
            c = dataSource.getConnection();
        } catch (SQLException e) {
            Log.log("Could not init database connection", e);
        }
        CONNECTION = c;
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mariadb://localhost:3306/" + BotConfig.DB_NAME);
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setUsername(BotConfig.DB_USER);
        config.setPassword(BotConfig.DB_PASS);
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
            throw new DevelopmentException("Could not select: " + sql, e);
        }
    }

    public static void query(String sql) {
        try{RUNNER.update(sql);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not query: " + sql, e);
        }
    }

    public static void insert(String sql) {
        try{RUNNER.update(sql);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not insert", e);
        }
    }
    public static String quote(String id){
        return Character.isDigit(id.charAt(0)) ? id : "'" + id + "'";
    }
}