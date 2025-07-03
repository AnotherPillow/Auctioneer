package com.anotherpillow.auctioneer.item.start;

import com.anotherpillow.auctioneer.Auctioneer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.inventoryaccess.component.BungeeComponentWrapper;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;

import java.util.List;

public class IncrementItem extends LoopingItem<Double> {

    private final List<Double> POSSIBLE_VALUES = List.of(
            1d,
            2.5d,
            5d,
            10d,
            15d,
            20d,
            25d,
            33d,
            50d,
            75d,
            100d
    );

    private final Material ITEM_TYPE = Material.DIAMOND;

    @Override
    protected List<Double> getPossibleValues() {
        return POSSIBLE_VALUES;
    }

    @Override
    protected Material getItemType() {
        return ITEM_TYPE;
    }

    @Override
    protected String getDisplayName() {
        return "§8§lIncrement: §b§n" + POSSIBLE_VALUES.get(index) + "%";
    }

    @Override
    protected String getSelectedRow(int i) {
        return "§a✓ §6§l " + POSSIBLE_VALUES.get(i) + "%";
    }

    @Override
    protected String getUnselectedRow(int i) {
        return "§7> §e " + POSSIBLE_VALUES.get(i) + "%";
    }
}