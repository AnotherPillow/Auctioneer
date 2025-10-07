package com.anotherpillow.auctioneer.item.listing;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.AuctionManager;
import com.anotherpillow.auctioneer.db.model.AuctionModel;
import com.anotherpillow.auctioneer.gui.BidGui;
import com.anotherpillow.auctioneer.gui.BiddingGUIManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

public class ListedItem extends AbstractItem {
    ItemStack decoratedStack = null;
    AuctionModel auction = null;
    Auctioneer plugin = null;

    public ListedItem(AuctionModel auction, Auctioneer plugin) {
        super();

        this.decoratedStack = auction.getOfferedItem();
        this.auction = auction;
        this.plugin = plugin;

        ItemMeta meta = this.decoratedStack.getItemMeta();
        meta.displayName(
                Component.empty().content(auction.getAuthorName() + (auction.getAuthorName().endsWith("s") ? "' " : "'s "))
                .append(meta.displayName() == null
                        ? Component.empty().content(this.decoratedStack.getI18NDisplayName())
                        : meta.displayName())
        );
        this.decoratedStack.setItemMeta(meta);
    }

    @Override
    public ItemProvider getItemProvider() {
        return new ItemBuilder(this.decoratedStack);
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {

        if (clickType.isLeftClick()) {
            // BiddingGUIManager.windows is a hashmap of auction id -> Gui
            BidGui gui = BiddingGUIManager.windows.get(this.auction.getUuid());

            if (auction == null) {
                player.sendMessage(Component.empty().content("Auction does not exist!").color(NamedTextColor.RED));
                return;
            }

            if (0 >= auction.getTimeoutDate() - System.currentTimeMillis()) {
                player.sendMessage(Component.empty().content("That auction has expired!").color(NamedTextColor.RED));
                return;
            };

            if (gui == null) {
                gui = new BidGui(this.plugin, auction);
                BiddingGUIManager.windows.put(this.auction.getUuid(), gui);
            }

            gui.render(player);
        }

//        notifyWindows(); // this will update the ItemStack that is displayed to the player
    }
}
