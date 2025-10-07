/**
 * Auctioneer source code Copyright (c) AnotherPillow 2025. All Rights Reserved
 * See `License.md` in the root of this repository for full details.
 */

package com.anotherpillow.auctioneer.db;

import com.anotherpillow.auctioneer.db.model.AuctionModel;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

public class AuctionManager {

    public static UUID submitAuction(StartGuiDataHolder holder, Player player) {
        UUID uid = UUID.randomUUID();

        try {
            DatabaseManager.insertAuction(uid.toString(), player.getUniqueId().toString(), player.getName(),
                    holder.getIncrementValue(), holder.getInitialPriceValue(), holder.getTimeoutValue().format(),
                    System.currentTimeMillis() + holder.getTimeoutValue().toMilliseconds(),
                    Base64.getEncoder().encodeToString(holder.getOfferedItem().serializeAsBytes())
            );
        } catch (SQLException e) {
            return null;
        }

        return uid;

    }

    public static boolean auctionExists(String uuid) {
        return DatabaseManager.auctionExists(uuid);
    }

    public static AuctionModel getAuction(String uuid) {
        try {
            return DatabaseManager.getAuctionByUuid(uuid);
        } catch (SQLException e) {
            return null;
        }
    }

    public static void updateTopBid(String auctionUUID, UUID topBidderUUID, String topBidderName, double topBidPrice) {
        try {
            DatabaseManager.updateTopBid(auctionUUID, topBidderUUID.toString(), topBidderName, topBidPrice);
        } catch (SQLException e) {
            return;
        }
    }

    public static List<AuctionModel> getActiveAuctions() {
        try {
            return DatabaseManager.getActiveAuctions();
        } catch (SQLException e) {
            return null;
        }
    }
}
