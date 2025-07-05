package com.anotherpillow.auctioneer.item.start;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.AuctionManager;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
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
import xyz.xenondevs.invui.window.Window;

import java.util.Base64;
import java.util.UUID;

public class ConfirmItem extends AbstractItem {
    protected final StartGuiDataHolder data;

    public ConfirmItem(StartGuiDataHolder data) {
        this.data = data;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemStack stack = new ItemStack(Material.EMERALD);
        ItemMeta meta = stack.getItemMeta();

        meta.addEnchant(Enchantment.BINDING_CURSE, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        stack.setItemMeta(meta);

        return new ItemBuilder(stack).setDisplayName("§2§lConfirm");
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
//        player.sendMessage("increment: " + data.getIncrementValue());
//        player.sendMessage("timeout: " + data.getTimeoutValue());
//        player.sendMessage("initial price: " + data.getInitialPriceValue());
//        player.sendMessage("offered item: " + Base64.getEncoder().encodeToString(data.getOfferedItem().serializeAsBytes()));

        UUID submitUUID = AuctionManager.submitAuction(data, player);
        if (submitUUID != null) {
            for (Window window : getWindows()) {
                window.close();
            }
            Component metaDisplayName = data.getOfferedItem().getItemMeta().displayName();
            Component displayName = metaDisplayName == null ?
                    Component.empty()
                            .content(data.getOfferedItem().getI18NDisplayName())
                            .color(NamedTextColor.WHITE)
                            .decorate(TextDecoration.UNDERLINED)
                    :
                    metaDisplayName;

            Bukkit.broadcast(Component.empty()
                    .content("=".repeat(40))
                    .decorate(TextDecoration.STRIKETHROUGH)
                    .color(NamedTextColor.GOLD)
            );
            Bukkit.broadcast(Component.empty().content("|").color(NamedTextColor.DARK_GRAY));
            Bukkit.broadcast(Component.empty()
                    .append(Component.empty().content("| ").color(NamedTextColor.DARK_GRAY))
                    .append(Component.empty()
                                    .content(player.getName())
                                    .decorate(TextDecoration.BOLD)
                                    .color(NamedTextColor.GREEN)
                            )
                    .append(Component.empty()
                                    .content(" just started an auction for a ")
                    )
                    .append(displayName.hoverEvent(data.getOfferedItem().asHoverEvent()))
                    .clickEvent(ClickEvent.runCommand("/auctioneer:viewauction " + submitUUID))
            );
            Bukkit.broadcast(Component.empty().content("|").color(NamedTextColor.DARK_GRAY));
            Bukkit.broadcast(Component.empty()
                    .append(Component.empty().content("| ").color(NamedTextColor.DARK_GRAY))
                    .append(Component.empty()
                            .content("Time remaining: ")
                            .color(NamedTextColor.GRAY)
                    )
                    .append(Component.empty()
                            .content(data.getTimeoutValue().format())
                            .color(NamedTextColor.DARK_GREEN)
                    )
                    .append(Component.empty()
                            .content(" - ")
                            .color(NamedTextColor.DARK_GRAY)
                    )
                    .append(Component.empty()
                            .content("Initial Cost: ")
                            .color(NamedTextColor.GRAY)
                    )
                    .append(Component.empty()
                            .content(Auctioneer.econ.format(data.getInitialPriceValue()))
                            .color(NamedTextColor.DARK_GREEN)
                    )
                    .clickEvent(ClickEvent.runCommand("/auctioneer:viewauction " + submitUUID))
            );
            Bukkit.broadcast(Component.empty().content("|").color(NamedTextColor.DARK_GRAY));
            Bukkit.broadcast(Component.empty()
                    .content("=".repeat(40))
                    .decorate(TextDecoration.STRIKETHROUGH)
                    .color(NamedTextColor.GOLD)
            );

        }

        notifyWindows(); // this will update the ItemStack that is displayed to the player
    }

}