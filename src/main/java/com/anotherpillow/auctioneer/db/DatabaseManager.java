/**
 * Auctioneer source code Copyright (c) AnotherPillow 2025. All Rights Reserved
 * See `License.md` in the root of this repository for full details.
 */

package com.anotherpillow.auctioneer.db;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.model.AuctionModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public static Connection connection;

    public static void connect() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:plugins/Auctioneer/auctioneer-database.db");
    }

    public static void setupTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS auctions ("
                + "    uuid   TEXT PRIMARY KEY,"
                + "    authorUUID  TEXT NOT NULL,"
                + "    authorNAME  TEXT NOT NULL,"
                + "    incrementPercent  REAL NOT NULL,"
                + "    initialPrice  REAL NOT NULL,"
                + "    timeoutFormat  TEXT NOT NULL,"
                + "    timeoutDate  INTEGER NOT NULL,"
                + "    offeredItemB64  TEXT NOT NULL,"
                + "    topBidderUUID  TEXT,"
                + "    topBidderName  TEXT,"
                + "    topBidPrice  REAL"
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
            double initialPrice, // Changed to double
            String timeoutFormat,
            long timeoutDate,
            String offeredItemB64
    ) throws SQLException {
        String sql =
                "INSERT INTO auctions (" +
                        "    uuid, authorUUID, authorNAME," +
                        "    incrementPercent, initialPrice," +
                        "    timeoutFormat," +
                        "    timeoutDate, offeredItemB64," +
                        "    topBidderUUID, topBidderName, topBidPrice" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid);
            pstmt.setString(2, authorUUID);
            pstmt.setString(3, authorName);
            pstmt.setDouble(4, incrementPercent);
            pstmt.setDouble(5, initialPrice); // Changed to setDouble
            pstmt.setString(6, timeoutFormat);
            pstmt.setLong(7, timeoutDate);
            pstmt.setString(8, offeredItemB64);
            pstmt.setString(9, null);
            pstmt.setString(10, null);
            pstmt.setObject(11, null, java.sql.Types.REAL); // Changed to REAL
            pstmt.executeUpdate();
        }
    }

    public static void updateTopBid(
            String auctionUUID,
            String topBidderUUID,
            String topBidderName,
            double topBidPrice // Changed to double
    ) throws SQLException {
        String sql = "UPDATE auctions SET " +
                "topBidderUUID = ?, " +
                "topBidderName = ?, " +
                "topBidPrice = ? " +
                "WHERE uuid = ?;";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, topBidderUUID);
            pstmt.setString(2, topBidderName);
            pstmt.setDouble(3, topBidPrice); // Changed to setDouble
            pstmt.setString(4, auctionUUID);
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

    public static AuctionModel getAuctionByUuid(String uuid) throws SQLException {
        String sql = "SELECT uuid, authorUUID, authorNAME, " +
                "incrementPercent, initialPrice, timeoutFormat, " +
                "timeoutDate, offeredItemB64, topBidderUUID, topBidderName, topBidPrice FROM auctions WHERE uuid = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, uuid);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new AuctionModel(
                            rs.getString("uuid"),
                            rs.getString("authorUUID"),
                            rs.getString("authorNAME"),
                            rs.getDouble("incrementPercent"),
                            rs.getInt("initialPrice"),
                            rs.getString("timeoutFormat"),
                            rs.getLong("timeoutDate"),
                            rs.getString("offeredItemB64"),
                            rs.getString("topBidderUUID"),
                            rs.getString("topBidderName"),
                            rs.getDouble("topBidPrice")
                    );
                } else return null;
            }
        }
    }

    public static List<AuctionModel> getActiveAuctions() throws SQLException {
        long now = System.currentTimeMillis();
        String sql = "SELECT uuid, authorUUID, authorNAME, " +
                "incrementPercent, initialPrice, timeoutFormat, " +
                "timeoutDate, offeredItemB64, topBidderUUID, topBidderName, topBidPrice " +
                "FROM auctions " +
                "WHERE timeoutDate > ? " +
                "ORDER BY timeoutDate ASC;";

        List<AuctionModel> results = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setLong(1, now);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    AuctionModel model = new AuctionModel(
                            rs.getString("uuid"),
                            rs.getString("authorUUID"),
                            rs.getString("authorNAME"),
                            rs.getDouble("incrementPercent"),
                            rs.getInt("initialPrice"),
                            rs.getString("timeoutFormat"),
                            rs.getLong("timeoutDate"),
                            rs.getString("offeredItemB64"),
                            rs.getString("topBidderUUID"),
                            rs.getString("topBidderName"),
                            rs.getDouble("topBidPrice")
                    );
                    results.add(model);
                }
            }
        }

        return results;
    }
}
