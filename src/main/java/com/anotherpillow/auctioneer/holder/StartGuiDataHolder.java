package com.anotherpillow.auctioneer.holder;

import com.anotherpillow.auctioneer.util.TimingEntry;
import org.bukkit.inventory.ItemStack;

public class StartGuiDataHolder {
    private double incrementValue = 0d;
    private int initialPriceValue = 0;
    private TimingEntry timeoutValue = null;

    private ItemStack offeredItem = null;

    public StartGuiDataHolder() {}

    public double getIncrementValue() {
        return incrementValue;
    }

    public void setIncrementValue(double incrementValue) {
        this.incrementValue = incrementValue;
    }

    public int getInitialPriceValue() {
        return initialPriceValue;
    }

    public void setInitialPriceValue(int initialPriceValue) {
        this.initialPriceValue = initialPriceValue;
    }

    public TimingEntry getTimeoutValue() {
        return timeoutValue;
    }

    public void setTimeoutValue(TimingEntry timeoutValue) {
        this.timeoutValue = timeoutValue;
    }

    public ItemStack getOfferedItem() {
        return offeredItem;
    }

    public void setOfferedItem(ItemStack offeredItem) {
        this.offeredItem = offeredItem;
    }
}