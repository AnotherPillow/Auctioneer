package com.anotherpillow.auctioneer.gui;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.AuctionManager;
import com.anotherpillow.auctioneer.db.DatabaseManager;
import com.anotherpillow.auctioneer.db.model.AuctionModel;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import com.anotherpillow.auctioneer.item.bidder.BidLevelItem;
import com.anotherpillow.auctioneer.item.bidder.CountItem;
import com.anotherpillow.auctioneer.item.bidder.CountdownItem;
import com.anotherpillow.auctioneer.item.start.ConfirmItem;
import com.anotherpillow.auctioneer.item.start.IncrementItem;
import com.anotherpillow.auctioneer.item.start.InitialPriceItem;
import com.anotherpillow.auctioneer.item.start.TimeoutItem;
import com.anotherpillow.auctioneer.util.CosmeticItems;
import org.bukkit.Bukkit;
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
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BidGui {

    public int SLOTS_PER_PAGE = 29; // 29 letter s in the layout
    public HashMap<Integer, Point> ITEM_COORDINATES = new HashMap<Integer, Point>();
    public int OFFSET_PAGES = 0;
    public List<BidLevelItem> currentPageBidItems = new ArrayList<>();

    private Auctioneer plugin = null;
    private Gui gui = null;
    private AuctionModel auction = null;

    public BidGui(Auctioneer plugin, AuctionModel auction) {
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

        this.auction = auction;
        this.plugin = plugin;

        this.gui = this.createGui();
    }

    private long getRemainingTime() {
        long now = System.currentTimeMillis();

        return this.auction.getTimeoutDate() - now;
    }

    private void populateGui(Gui gui) {
        for (int i = 0; i < SLOTS_PER_PAGE; ++i) {
            int oi = i + (OFFSET_PAGES * SLOTS_PER_PAGE);
            Point xy = ITEM_COORDINATES.get(i);
//            Item item = new SimpleItem(
//                    new ItemBuilder(
//                            Material.GOLD_INGOT
//                    ).setDisplayName(Auctioneer.econ.format(
////                            this.auction.getInitialPrice() + (this.auction.getIncrementPercent() * oi)
//                            // percent is stored in db as x not 0.x, so divide by 100 and do P(i+1)^t
//                            this.auction.getInitialPrice() * Math.pow(this.auction.getIncrementPercent() / 100 + 1, oi)
//                    ))
//            );

//            System.out.println("[Auctioneer] BidGui:100> i: " + i + " oi: " + oi + " xy: " + (xy == null ? null : xy.toString()) + " item: " + item.getItemProvider().get().getType().toString());


            BidLevelItem item = new BidLevelItem(
                    i, oi, xy,
                    this.auction.getInitialPrice() * Math.pow(this.auction.getIncrementPercent() / 100 + 1, oi), this
            );
            gui.setItem(xy.x, xy.y, item);
            currentPageBidItems.add(item);
        }
    }

    public Gui createGui() {
        if (0 >= getRemainingTime()) return null;

        Gui.Builder.Normal builder = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                        "t # p # b # i # C",
                        "s # s s s # s s s",
                        "s # s # s # s # s",
                        "s # s # s # s # s",
                        "s # s # s # s # s",
                        "s s s # s s s # s")
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                .addIngredient('b', this.auction.getOfferedItem())
                .addIngredient('C', new CountItem())
                .addIngredient('t', new CountdownItem(this.auction.getTimeoutDate(), this))
                ;

        Gui gui = builder.build();

        populateGui(gui);

        return gui;
    }

    public void onBid(int index, String username, UUID uuid, double value) {
        if (index == SLOTS_PER_PAGE - 1) {
            this.OFFSET_PAGES++;
            this.currentPageBidItems.clear();
            populateGui(this.gui);
        }
        AuctionManager.updateTopBid(this.auction.getUuid(), uuid, username, value);
        // refresh bid data into model, unoptimised but won't fail
        this.auction = AuctionManager.getAuction(this.auction.getUuid());
    }

    public void render(Player player) {
        if (0 >= getRemainingTime()) return;

        Window window = Window.single()
                .setViewer(player)
                .setTitle("Start an Auction")
                .setGui(gui)
                .build();
        window.open();
    }

    public void timerExpired() {
        this.gui.findAllCurrentViewers().forEach((Player player) -> {
            player.sendMessage("The auction expired! The winner is " + this.auction.getTopBidderName() +
                    " with a price of " + Auctioneer.econ.format(this.auction.getTopBidPrice()));
            // if (player.getUniqueId() == UUID.fromString("")) {}
        });
        Bukkit.getPlayer(this.auction.getTopBidderUUID()).getInventory().addItem(this.auction.getOfferedItem());
        this.gui.closeForAllViewers();
    }
}
