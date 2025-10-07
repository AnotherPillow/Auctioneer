package com.anotherpillow.auctioneer.gui;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.AuctionManager;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import com.anotherpillow.auctioneer.item.listing.BackItem;
import com.anotherpillow.auctioneer.item.listing.ForwardItem;
import com.anotherpillow.auctioneer.item.listing.ListedItem;
import com.anotherpillow.auctioneer.item.start.ConfirmItem;
import com.anotherpillow.auctioneer.item.start.IncrementItem;
import com.anotherpillow.auctioneer.item.start.InitialPriceItem;
import com.anotherpillow.auctioneer.item.start.TimeoutItem;
import com.anotherpillow.auctioneer.util.CosmeticItems;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.window.Window;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListingsGui {
    private Auctioneer plugin = null;

    public ListingsGui(Auctioneer plugin) {
        this.plugin = plugin;
    }

    public void render(Player player) {
//        List<Item> items = Arrays.stream(Material.values())
//                .filter(material -> !material.isAir() && material.isItem())
//                .map(material -> new SimpleItem(new ItemBuilder(material)))
//                .collect(Collectors.toList());

        List<Item> items = AuctionManager.getActiveAuctions()
                .stream()
                .map(auction -> new ListedItem(auction, this.plugin))
                .collect(Collectors.toList());


        Gui gui = PagedGui.items() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                        "# # # # # # # # #",
                        "# c c c c c c c #",
                        "# c c c c c c c #",
                        "# c c c c c c c #",
                        "# c c c c c c c #",
                        "# # # < # > # # #")
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                .addIngredient('c', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(items)
                .build()
                ;

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Explore Auctions")
                .setGui(gui)
                .build();

        window.open();
    }
}
