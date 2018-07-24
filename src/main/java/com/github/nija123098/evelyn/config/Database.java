package com.github.nija123098.evelyn.config;

import com.github.nija123098.evelyn.botconfiguration.ConfigProvider;
import com.github.nija123098.evelyn.exception.DevelopmentException;
import com.github.nija123098.evelyn.util.CallBuffer;
import com.github.nija123098.evelyn.util.Log;
import com.github.nija123098.evelyn.util.ThreadHelper;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Controls interaction with the database.
 *
 * @author Soarnir
 * @author nija123098
 * @since 1.0.0
 */
public class Database {
    private static final CallBuffer CALL_BUFFER = new CallBuffer("Database", 250);
    private static final AtomicReference<Connection> CONNECTION = new AtomicReference<>();
    private static final AtomicReference<QueryRunner> RUNNER = new AtomicReference<>();
    private static final AtomicReference<HikariDataSource> DATA_SOURCE = new AtomicReference<>();
    private static final AtomicBoolean INITIALIZED = new AtomicBoolean();
    private static final Object INITIALIZE_LOCK = new Object();

    public static void init() {
        Executors.newSingleThreadScheduledExecutor(r -> ThreadHelper.getDemonThreadSingle(r, "DB-Restart-Executor")).scheduleAtFixedRate(Database::initializeDB, 0, 4, TimeUnit.HOURS);
    }

    private static void initializeDB() {
        INITIALIZED.set(false);
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
            dataSource.setZeroDateTimeBehavior("CONVERT_TO_NULL");
            dataSource.setCharacterEncoding("UTF-8"); // dataSource.setUseUnicode(true);
            dataSource.setServerTimezone("UTC");
            dataSource.setGenerateSimpleParameterMetadata(true);
            c = dataSource.getConnection();
        } catch (SQLException e) {
            Log.log("Could not init database connection", e);
        }
        Connection oldConnection = CONNECTION.getAndSet(c);
        HikariConfig config = new HikariConfig();
        config.setMaximumPoolSize(20);
        config.setJdbcUrl("jdbc:mariadb://" + serverIP + ":" + serverPort + "/" + dbName + "?characterEncoding=utf8mb4");
        config.setDriverClassName("org.mariadb.jdbc.Driver");
        config.setUsername(username);
        config.setPassword(password);
        HikariDataSource source = new HikariDataSource(config);
        HikariDataSource oldSource = DATA_SOURCE.getAndSet(source);
        RUNNER.set(new QueryRunner(source));
        INITIALIZED.set(true);
        query("SET NAMES utf8mb4");
        synchronized (INITIALIZE_LOCK) {
            INITIALIZE_LOCK.notifyAll();
        }
        if (oldConnection != null) {
            try {
                oldSource.close();
                oldConnection.close();
            } catch (SQLException e) {
                Log.log("Exception closing connection to DB", e);
            }
        }
    }

    public static void checkInitialization() {
        if (!INITIALIZED.get()) {
            try {
                synchronized (INITIALIZE_LOCK) {
                    INITIALIZE_LOCK.wait();
                }
            } catch (InterruptedException e) {
                Log.log("Unexpected interrupt while waiting for init lock on DB");
            }
        }
    }

    public static void bufferCall(Runnable runnable) {
        CALL_BUFFER.call(runnable);
    }

    public static Connection getConnection() {
        checkInitialization();
        return CONNECTION.get();
    }

    public static <E> E select(String sql, ResultSetHandler<E> handler) {
        checkInitialization();
        try{return RUNNER.get().query(sql, handler);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not select: ERROR: " + e.getErrorCode() + " " + sql, e);
        }
    }

    public static void query(String sql) {
        checkInitialization();
        try{RUNNER.get().update(sql);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not query: ERROR: " + e.getErrorCode() + " " + sql, e);
        }
    }

    public static void insert(String sql) {
        checkInitialization();
        try{RUNNER.get().update(sql);
        } catch (SQLException e) {
            throw new DevelopmentException("Could not insert: ERROR: " + e.getErrorCode() + " " + sql, e);
        }
    }
    public static String quote(String id) {
        return Character.isDigit(id.charAt(0)) ? id : "'" + id + "'";
    }
}