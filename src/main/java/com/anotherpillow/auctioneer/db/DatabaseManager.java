package com.anotherpillow.auctioneer.db;

import com.anotherpillow.auctioneer.Auctioneer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    public static Connection connection;

    public static void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:plugins/auctioneer/database.db");
    }

    public static void setupTables() throws SQLException {
        String sql = ""
                + "CREATE TABLE IF NOT EXISTS player_counters ("
                + "    uuid   TEXT PRIMARY KEY,"
                + "    count  INTEGER NOT NULL"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }
}
