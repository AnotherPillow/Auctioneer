package com.anotherpillow.auctioneer.gui;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import com.anotherpillow.auctioneer.item.bidder.CountItem;
import com.anotherpillow.auctioneer.item.start.ConfirmItem;
import com.anotherpillow.auctioneer.item.start.IncrementItem;
import com.anotherpillow.auctioneer.item.start.InitialPriceItem;
import com.anotherpillow.auctioneer.item.start.TimeoutItem;
import com.anotherpillow.auctioneer.util.CosmeticItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BidGui {

    private int SLOTS_PER_PAGE = 29; // 29 letter s in the layout
    private HashMap<Integer, Point> ITEM_COORDINATES = new HashMap<Integer, Point>();
    private int OFFSET_PAGES = 0;

    private Auctioneer plugin = null;
    private Gui gui = null;

    public BidGui(Auctioneer plugin) {
//        ConfigurationSection modes = plugin.getConfig().getConfigurationSection("modes");

        // I am so good at code that is super duper readable
        ITEM_COORDINATES.put(0, new Point(0, 1));
        ITEM_COORDINATES.put(1, new Point(0, 2));
        ITEM_COORDINATES.put(2, new Point(0, 3));
        ITEM_COORDINATES.put(3, new Point(0, 4));
        ITEM_COORDINATES.put(4, new Point(0, 5));
        ITEM_COORDINATES.put(5, new Point(1, 5));
        ITEM_COORDINATES.put(6, new Point(2, 5));
        ITEM_COORDINATES.put(7, new Point(2, 4));
        ITEM_COORDINATES.put(8, new Point(2, 3));
        ITEM_COORDINATES.put(9, new Point(2, 2));
        ITEM_COORDINATES.put(10, new Point(2, 1));
        ITEM_COORDINATES.put(11, new Point(3, 1));
        ITEM_COORDINATES.put(12, new Point(4, 1));
        ITEM_COORDINATES.put(13, new Point(4, 2));
        ITEM_COORDINATES.put(14, new Point(4, 3));
        ITEM_COORDINATES.put(15, new Point(4, 4));
        ITEM_COORDINATES.put(16, new Point(4, 5));
        ITEM_COORDINATES.put(17, new Point(5, 5));
        ITEM_COORDINATES.put(18, new Point(6, 5));
        ITEM_COORDINATES.put(19, new Point(6, 4));
        ITEM_COORDINATES.put(20, new Point(6, 3));
        ITEM_COORDINATES.put(21, new Point(6, 2));
        ITEM_COORDINATES.put(22, new Point(6, 1));
        ITEM_COORDINATES.put(23, new Point(7, 1));
        ITEM_COORDINATES.put(24, new Point(8, 1));
        ITEM_COORDINATES.put(25, new Point(8, 2));
        ITEM_COORDINATES.put(26, new Point(8, 3));
        ITEM_COORDINATES.put(27, new Point(8, 4));
        ITEM_COORDINATES.put(28, new Point(8, 5));

        this.plugin = plugin;
        this.gui = this.createGui();

    }

    public Gui createGui() {
        List<Item> items = Arrays.stream(Material.values())
                .filter(material -> !material.isAir() && material.isItem())
                .map(material -> new SimpleItem(new ItemBuilder(material)))
                .collect(Collectors.toList());

        Gui.Builder.Normal builder = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                        "# # # # C # # # #",
                        "s # s s s # s s s",
                        "s # s # s # s # s",
                        "s # s # s # s # s",
                        "s # s # s # s # s",
                        "s s s # s s s # s")
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                .addIngredient('C', new CountItem())
                ;

        Gui gui = builder.build();

        for (int i = 0; i < SLOTS_PER_PAGE; ++i) {
            int oi = i + (OFFSET_PAGES * SLOTS_PER_PAGE);
            Point xy = ITEM_COORDINATES.get(i);
            Item item = items.get(oi);

            System.out.println("[Auctioneer] BidGui:100> i: " + i + " oi: " + oi + " xy: " + (xy == null ? null : xy.toString()) + " item: " + item.getItemProvider().get().getType().toString());

            gui.setItem(xy.x, xy.y, item);
        }

        return gui;
    }

    public void render(Player player) {
        Window window = Window.single()
                .setViewer(player)
                .setTitle("Start an Auction")
                .setGui(gui)
                .build();
        window.open();
    }
}
