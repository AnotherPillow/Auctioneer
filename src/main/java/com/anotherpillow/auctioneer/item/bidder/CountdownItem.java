package com.anotherpillow.auctioneer.item.bidder;

import com.anotherpillow.auctioneer.gui.BidGui;
import com.anotherpillow.auctioneer.util.TimingEntry;
import org.bukkit.Material;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.AutoUpdateItem;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

public class CountdownItem extends AutoUpdateItem {
    private final long endMilliseconds;
    private final BidGui bidGui;
    private boolean isExpired = false;

    public CountdownItem(long endMilliseconds, BidGui bidGui) {
        // period is in ticks, so assume 20 TPS, not a big deal if slower
        // builderSupplier can be null because it's only called in getItemProvider, which this overrides
        super(20, null);
        this.endMilliseconds = endMilliseconds;
        this.bidGui = bidGui;
    }

    private long getRemainingTime() {
        long now = System.currentTimeMillis();

        return endMilliseconds - now;
    }

    @Override
    public ItemProvider getItemProvider() {
        if (this.isExpired) return null;

        long remaining =this.getRemainingTime();
        if (0 >= remaining) {
            this.isExpired = true;
            this.bidGui.timerExpired();
        }

        ItemBuilder builder = new ItemBuilder(Material.CLOCK);
        TimingEntry te = new TimingEntry(remaining, TimeUnit.MILLISECONDS);
        builder.setDisplayName("§8§lTime remaining: §b§n" + te.format());

        return builder;
    }
}
