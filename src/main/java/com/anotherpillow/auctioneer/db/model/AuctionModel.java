/**
 * Auctioneer source code Copyright (c) AnotherPillow 2025. All Rights Reserved
 * See `License.md` in the root of this repository for full details.
 */

package com.anotherpillow.auctioneer.db.model;

import org.bukkit.inventory.ItemStack;

import java.util.Base64;
import java.util.UUID;

public class AuctionModel {
    private final String uuid;
    private final String authorUUID;
    private final String authorName;
    private final double incrementPercent;
    private final int initialPrice;
    private final String timeoutFormat;
    private final long timeoutDate;
    private final String offeredItemB64;
    private final String topBidderUUID;
    private final String topBidderName;
    private final double topBidPrice;

    public AuctionModel(
            String uuid,
            String authorUUID,
            String authorName,
            double incrementPercent,
            int initialPrice,
            String timeoutFormat,
            long timeoutDate,
            String offeredItemB64,
            String topBidderUUID,
            String topBidderName,
            double topBidPrice
    ) {
        this.uuid = uuid;
        this.authorUUID = authorUUID;
        this.authorName = authorName;
        this.incrementPercent = incrementPercent;
        this.initialPrice = initialPrice;
        this.timeoutFormat = timeoutFormat;
        this.timeoutDate = timeoutDate;
        this.offeredItemB64 = offeredItemB64;
        this.topBidderUUID = topBidderUUID;
        this.topBidderName = topBidderName;
        this.topBidPrice = topBidPrice;
    }

    // Getters
    public String getUuid() { return uuid; }
    public String getAuthorUUID() { return authorUUID; }
    public String getAuthorName() { return authorName; }
    public double getIncrementPercent() { return incrementPercent; }
    public int getInitialPrice() { return initialPrice; }
    public String getTimeoutFormat() { return timeoutFormat; }
    public long getTimeoutDate() { return timeoutDate; }
    public String getOfferedItemB64() { return offeredItemB64; }
    public ItemStack getOfferedItem() {
        // Base64.getEncoder().encodeToString(holder.getOfferedItem().serializeAsBytes())
        byte[] b = Base64.getDecoder().decode(this.offeredItemB64);

        return ItemStack.deserializeBytes(b);
    };
    public UUID getTopBidderUUID() { return this.topBidderUUID == null ? null : UUID.fromString(this.topBidderUUID); }
    public String getTopBidderName() { return this.topBidderName; }
    public double getTopBidPrice() { return this.topBidPrice; }
}