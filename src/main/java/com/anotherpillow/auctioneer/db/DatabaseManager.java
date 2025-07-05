package com.anotherpillow.auctioneer.db;

import com.anotherpillow.auctioneer.Auctioneer;

import java.sql.*;

public class DatabaseManager {
    public static Connection connection;

    public static void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:plugins/auctioneer/database.db");
    }

    public static void setupTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS auctions ("
                + "    uuid   TEXT PRIMARY KEY,"
                + "    authorUUID  TEXT NOT NULL,"
                + "    authorNAME  TEXT NOT NULL,"
                + "    incrementPercent  REAL NOT NULL,"
                + "    initialPrice  INTEGER NOT NULL,"
                + "    timeoutFormat  TEXT NOT NULL,"
                + "    timeoutDate  INTEGER NOT NULL,"
                + "    offeredItemB64  TEXT NOT NULL"
                + ");";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
        }
    }

    public static void insertAuction(
            String uuid,
            String authorUUID,
            String authorName,
            double incrementPercent,
            int initialPrice,
            String timeoutFormat,
            long timeoutDate,
            String offeredItemB64
    ) throws SQLException {
        String sql =
                "INSERT INTO auctions (" +
                        "    uuid, authorUUID, authorNAME," +
                        "    incrementPercent, initialPrice," +
                        "    timeoutFormat," +
                        "    timeoutDate, offeredItemB64" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, authorUUID);
            pstmt.setString(3, authorName);
            pstmt.setDouble(4, incrementPercent);
            pstmt.setInt(5, initialPrice);
            pstmt.setString(6, timeoutFormat);
            pstmt.setLong(7, timeoutDate);
            pstmt.setString(8, offeredItemB64);
            pstmt.executeUpdate();
        }
    }

    public static boolean auctionExists(String uuid) {
        String sql = "SELECT 1 FROM auctions WHERE uuid = ? LIMIT 1;";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) { return false; }
    }
}
