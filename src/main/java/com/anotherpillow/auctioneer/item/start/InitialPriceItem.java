/**
 * Auctioneer source code Copyright (c) AnotherPillow 2025. All Rights Reserved
 * See `License.md` in the root of this repository for full details.
 */
package com.anotherpillow.auctioneer.item.start;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import org.bukkit.Material;

import java.util.List;

public class InitialPriceItem extends LoopingItem<Integer> {

    private final List<Integer> POSSIBLE_VALUES = List.of(
            100,
            500,
            1000,
            10_000,
            25_000,
            50_000,
            100_000,
            250_000,
            500_000,
            750_000,
            1_000_000
    );

    private final Material ITEM_TYPE = Material.DIAMOND_BLOCK;

    public InitialPriceItem(StartGuiDataHolder data) {
        super(data);
        data.setInitialPriceValue(this.POSSIBLE_VALUES.get(this.index));
    }

    @Override
    protected List<Integer> getPossibleValues() {
        return POSSIBLE_VALUES;
    }

    @Override
    protected Material getItemType() {
        return ITEM_TYPE;
    }

    @Override
    protected String getDisplayName() {
        return "§8§lInitial Price: §b§n" + Auctioneer.econ.format(POSSIBLE_VALUES.get(index));
    }

    @Override
    protected String getSelectedRow(int i) {
        return "§a✓ §6§l " + Auctioneer.econ.format(POSSIBLE_VALUES.get(i));
    }

    @Override
    protected String getUnselectedRow(int i) {
        return "§7> §e " + Auctioneer.econ.format(POSSIBLE_VALUES.get(i));
    }

    @Override
    protected void updateSelected(Integer value) {
        this.data.setInitialPriceValue(value);
    }
}