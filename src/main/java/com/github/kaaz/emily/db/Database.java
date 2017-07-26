package com.github.kaaz.emily.db;

import com.github.kaaz.emily.exeption.DevelopmentException;
import com.github.kaaz.emily.launcher.BotConfig;
import com.github.kaaz.emily.util.Log;
import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Soarnir on 16/7/17.
 */
public class Database {
    private static final Connection CONNECTION;
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
        query("SET NAMES utf8mb4");
    }

    public static void init() {
    }

    public static Connection getConnection() {
        return CONNECTION;
    }

    public static void ensureTableExistence(String name, int idSize, int valueSize) {
        try (ResultSet rs = CONNECTION.getMetaData().getTables(null, null, name, null)) {
            while (rs.next()) {
                String tName = rs.getString("TABLE_NAME");
                if (tName != null && tName.equals(name)) return;
            }
            Database.query("CREATE TABLE `" + name + "` (id VARCHAR(" + idSize + "), value VARCHAR(" + valueSize + "), millis BIGINT)");
        } catch (SQLException e) {
            throw new DevelopmentException("Could not ensure table existence", e);
        }
    }

    public static ResultSet select(String sql) {
        try{return CONNECTION.prepareStatement(sql).executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getErrorCode());
            throw new DevelopmentException("Could not select: " + sql, e);
        }
    }

    public static int query(String sql) {
        try{return CONNECTION.createStatement().executeUpdate(sql);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not query: " + sql, e);
        }
    }

    public static int insert(String sql) {
        try (PreparedStatement query = CONNECTION.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            query.executeUpdate();
            ResultSet rs = query.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new DevelopmentException("Could not insert", e);
        }
        return -1;
    }
    public static String quote(String id){
        return Character.isDigit(id.charAt(0)) ? id : "'" + id + "'";
    }
}