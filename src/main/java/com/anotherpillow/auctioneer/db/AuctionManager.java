package com.anotherpillow.auctioneer.db;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.model.AuctionModel;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.Base64;
import java.util.UUID;

public class AuctionManager {

    public static UUID submitAuction(StartGuiDataHolder holder, Player player) {
        UUID uid = UUID.randomUUID();

        try {
            DatabaseManager.insertAuction(uid.toString(), player.getName(), player.getUniqueId().toString(),
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
}
