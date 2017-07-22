package com.github.kaaz.emily.db;

import com.github.kaaz.emily.launcher.BotConfig;

import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Soarnir on 16/7/17.
 */



public class MySQLMain {


    private static final String DEFAULT_CONNECTION = "discord";
    private static HashMap<String, MySQLAccess> connections = new HashMap<>();

    public static MySQLAccess get(String key) {
        if (connections.containsKey(key)) {
            return connections.get(key);
        }
        System.out.println(String.format("The MySQL connection '%s' is not set!", key));
        return null;
    }

    public static MySQLAccess get() {
        return connections.get(DEFAULT_CONNECTION);
    }


    public static void init() {
        connections.clear();
        connections.put(DEFAULT_CONNECTION, new MySQLAccess(BotConfig.DB_HOST, BotConfig.DB_USER, BotConfig.DB_PASS, BotConfig.DB_NAME));

        try {
            get().query("SET NAMES utf8mb4");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("COULD NOT SET utf8mb4");
        }
    }

}
