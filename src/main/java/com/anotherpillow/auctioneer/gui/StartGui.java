package com.anotherpillow.auctioneer.gui;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import com.anotherpillow.auctioneer.item.start.ConfirmItem;
import com.anotherpillow.auctioneer.item.start.IncrementItem;
import com.anotherpillow.auctioneer.item.start.InitialPriceItem;
import com.anotherpillow.auctioneer.item.start.TimeoutItem;
import com.anotherpillow.auctioneer.util.CosmeticItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.stream.Stream;

public class StartGui {
    private Auctioneer plugin = null;

    public StartGui(Auctioneer plugin) {
//        ConfigurationSection modes = plugin.getConfig().getConfigurationSection("modes");
        this.plugin = plugin;
    }

    public void render(Player player) {
        ItemStack held = player.getItemInHand();
        if (held.getType() == Material.AIR) {
            player.sendMessage(Component.empty()
                    .content("Hold an item to put up for auction.")
                    .color(NamedTextColor.RED));
            return;
        }

        StartGuiDataHolder data = new StartGuiDataHolder();
        data.setOfferedItem(held);

        Gui.Builder.Normal builder = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                        "# # # # # # # # #",
                        "# # # # ! # # # #",
                        "# # # # # # # # #",
                        "# # i # s # t # #",
                        "# # # # # # # # #",
                        "# # # # C # # # ,")
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                .addIngredient('!', new SimpleItem(new ItemBuilder(held)))
                .addIngredient(',', CosmeticItems.getBalanceNugget(Auctioneer.econ.getBalance(player)))
                .addIngredient('i', new InitialPriceItem(data))
                .addIngredient('s', new IncrementItem(data))
                .addIngredient('t', new TimeoutItem(data))
                .addIngredient('C', new ConfirmItem(data))
        ;

        Gui gui = builder.build();

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Start an Auction")
                .setGui(gui)
                .build();

        window.open();
    }
}