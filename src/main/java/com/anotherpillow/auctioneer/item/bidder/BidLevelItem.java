package com.anotherpillow.auctioneer.item.bidder;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.gui.BidGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

import java.awt.*;

public class BidLevelItem extends AbstractItem {
    private int index;
    private int indexWithOffset;
    private Point position;
    private double price;
    private BidGui gui;

    private boolean hasPlacedBid = false;
    private String placedBidusername = null;

    public BidLevelItem(int index, int indexWithOffset, Point position, double price, BidGui gui) {
        super();

        this.index = index;
        this.indexWithOffset = indexWithOffset;
        this.position = position;
        this.price = price;
        this.gui = gui;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemStack stack = new ItemStack(Material.GOLD_INGOT);

        if (this.hasPlacedBid) {
            ItemMeta meta = stack.getItemMeta();

            meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            stack.setItemMeta(meta);

            return new ItemBuilder(stack).setDisplayName(String.format("§7§l%s §r§8by §7%s",
                    Auctioneer.econ.format(this.price),
                    this.placedBidusername
            ));
        } else {
            return new ItemBuilder(stack).setDisplayName("§6§l" + Auctioneer.econ.format(this.price));
        }


    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (this.hasPlacedBid) return;
        if (this.price > Auctioneer.econ.getBalance(player)) {
            player.sendMessage(Component.empty()
                    .content("You can't afford that!")
                    .color(NamedTextColor.RED));
            return;
        }

        if (clickType.isLeftClick()) {
            this.hasPlacedBid = true;
            this.placedBidusername = player.getName();
            this.backfillSelected();
            this.gui.onBid(this.index, player.getName(), player.getUniqueId(), this.price);
        }

        notifyWindows(); // this will update the ItemStack that is displayed to the player
    }

    private void backfillSelected() {
        for (int i = 0; i < this.index; i++) {
            BidLevelItem item = this.gui.currentPageBidItems.get(i);

            if (item.hasPlacedBid) continue;

            item.hasPlacedBid = true;
            item.placedBidusername = this.placedBidusername;
            item.notifyWindows();
        }
    }
}
