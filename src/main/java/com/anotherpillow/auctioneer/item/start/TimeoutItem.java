package com.anotherpillow.auctioneer.item.start;

import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import com.anotherpillow.auctioneer.util.TimingEntry;
import org.bukkit.Material;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeoutItem extends LoopingItem<TimingEntry> {
    private final List<TimingEntry> POSSIBLE_VALUES = List.of(
            new TimingEntry(30, TimeUnit.SECONDS),
            new TimingEntry(1, TimeUnit.MINUTES),
            new TimingEntry(2, TimeUnit.MINUTES),
            new TimingEntry(5, TimeUnit.MINUTES),
            new TimingEntry(10, TimeUnit.MINUTES),
            new TimingEntry(15, TimeUnit.MINUTES),
            new TimingEntry(30, TimeUnit.MINUTES),
            new TimingEntry(1, TimeUnit.HOURS),
            new TimingEntry(6, TimeUnit.HOURS),
            new TimingEntry(12, TimeUnit.HOURS),
            new TimingEntry(24, TimeUnit.HOURS),
            new TimingEntry(48, TimeUnit.HOURS)
    );

    private final Material ITEM_TYPE = Material.CLOCK;

    public TimeoutItem(StartGuiDataHolder data) {
        super(data);
        data.setTimeoutValue(this.POSSIBLE_VALUES.get(this.index));
    }

    @Override
    protected List<TimingEntry> getPossibleValues() {
        return POSSIBLE_VALUES;
    }

    @Override
    protected Material getItemType() {
        return ITEM_TYPE;
    }

    @Override
    protected String getDisplayName() {
        return "§8§lTimeout: §b§n" + POSSIBLE_VALUES.get(index).format();
    }

    @Override
    protected String getSelectedRow(int i) {
        return "§a✓ §6§l " + POSSIBLE_VALUES.get(i).format();
    }

    @Override
    protected String getUnselectedRow(int i) {
        return "§7> §e " + POSSIBLE_VALUES.get(i).format();
    }

    @Override
    protected void updateSelected(TimingEntry value) {
        this.data.setTimeoutValue(value);
    }
}
