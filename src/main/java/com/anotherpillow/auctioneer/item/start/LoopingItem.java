/**
 * Auctioneer source code Copyright (c) AnotherPillow 2025. All Rights Reserved
 * See `License.md` in the root of this repository for full details.
 */
package com.anotherpillow.auctioneer.item.start;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.util.List;

public abstract class LoopingItem<T> extends AbstractItem {

    protected int index = 2;
    protected final StartGuiDataHolder data;

    // Abstract methods that subclasses must implement
    protected abstract List<T> getPossibleValues();
    protected abstract Material getItemType();
    protected abstract String getDisplayName();
    protected abstract String getSelectedRow(int i);
    protected abstract String getUnselectedRow(int i);
    protected abstract void updateSelected(T value);

    public LoopingItem(StartGuiDataHolder data) {
        super();
        this.data = data;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder builder = new ItemBuilder(this.getItemType()).setDisplayName(this.getDisplayName());
        List<T> values = getPossibleValues();
        for (int i = 0; i < values.size(); i++) {
            if (index == i) {
                builder.addLoreLines(this.getSelectedRow(i));
            } else {
                builder.addLoreLines(this.getUnselectedRow(i));
            }
        }
        return builder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        List<T> values = getPossibleValues();
        if (clickType.isLeftClick()) {
            index = Math.min(index + 1, values.size() - 1);
        } else {
            index = Math.max(index - 1, 0);
        }

        this.updateSelected(values.get(index));

        notifyWindows();
    }
}