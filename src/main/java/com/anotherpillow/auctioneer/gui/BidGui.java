/**
 * Auctioneer source code Copyright (c) AnotherPillow 2025. All Rights Reserved
 * See `License.md` in the root of this repository for full details.
 */

package com.anotherpillow.auctioneer.gui;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.AuctionManager;
import com.anotherpillow.auctioneer.db.model.AuctionModel;
import com.anotherpillow.auctioneer.item.bidder.BidLevelItem;
import com.anotherpillow.auctioneer.item.bidder.CountdownItem;
import com.anotherpillow.auctioneer.util.Conversion;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BidGui {

    public int SLOTS_PER_PAGE = 29; // 29 letter s in the layout
    public HashMap<Integer, Point> ITEM_COORDINATES = new HashMap<Integer, Point>();
    public int OFFSET_PAGES = 0;
    public List<BidLevelItem> currentPageBidItems = new ArrayList<>();

    private Auctioneer plugin = null;
    private Gui gui = null;
    private AuctionModel auction = null;

    private boolean isAlreadyExpired = false;

    public BidGui(Auctioneer plugin, AuctionModel auction) {
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
                        "t # # # b # # # #",
                        "s # s s s # s s s",
                        "s # s # s # s # s",
                        "s # s # s # s # s",
                        "s # s # s # s # s",
                        "s s s # s s s # s")
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                .addIngredient('b', this.auction.getOfferedItem())
                .addIngredient('t', new CountdownItem(this.auction.getTimeoutDate(), this))
                ;

        Gui gui = builder.build();

        populateGui(gui);

        // catch if it isn't counting (no viewers, i think?)
        Bukkit.getScheduler().runTaskLater(this.plugin, this::timerExpired, Conversion.ticksUntil(this.auction.getTimeoutDate()) + 20);

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
                .setTitle("Bid")
                .setGui(gui)
                .build();
        window.open();
    }

    public void timerExpired() {
        if (isAlreadyExpired) return;
        isAlreadyExpired = true;
        Component metaDisplayName = this.auction.getOfferedItem().getItemMeta().displayName();
        Component displayName = metaDisplayName == null ?
                Component.empty()
                        .content(this.auction.getOfferedItem().getI18NDisplayName())
                        .color(NamedTextColor.WHITE)
                        .decorate(TextDecoration.UNDERLINED)
                :
                metaDisplayName;

        UUID topBidder = this.auction.getTopBidderUUID();

        if (topBidder != null) {
            System.out.println("top bidder uuid? " + topBidder.toString());

            this.gui.findAllCurrentViewers().forEach((Player player) -> {
                if (!Objects.equals(player.getUniqueId().toString(), topBidder.toString()))
                    player.sendMessage("The auction expired! The winner is " + this.auction.getTopBidderName() +
                            " with a price of " + Auctioneer.econ.format(this.auction.getTopBidPrice()));
            });

            Player onlineBidderPlayer = Bukkit.getPlayer(this.auction.getTopBidderName());
            OfflinePlayer offlineBidderPlayer = Bukkit.getOfflinePlayer(this.auction.getTopBidderName());

            if (!Auctioneer.econ.hasAccount(offlineBidderPlayer)) {
                Auctioneer.econ.createPlayerAccount(offlineBidderPlayer);
            }

            EconomyResponse resp = Auctioneer.econ.withdrawPlayer(offlineBidderPlayer, this.auction.getTopBidPrice());
            if (resp == null || resp.type != EconomyResponse.ResponseType.SUCCESS) {
                String reason = (resp == null) ? "No response from economy." : resp.errorMessage;
                onlineBidderPlayer.sendMessage(Component.text(
                        "Payment failed: " + reason, NamedTextColor.RED));
                // Optionally log
                Bukkit.getLogger().warning("[Auctioneer] Withdraw failed for "
                        + offlineBidderPlayer.getName() + " amount=" + this.auction.getTopBidPrice() + " reason=" + reason);
                return;
            }
            onlineBidderPlayer.sendMessage(Component.empty()
                    .append(
                            Component.empty()
                                    .color(NamedTextColor.GREEN)
                                    .content("You won " + this.auction.getAuthorName() + "'s auction for ")
                    )
                    .append(displayName)
                    .append(
                            Component.empty()
                                    .color(NamedTextColor.GREEN)
                                    .content(" with a bid of ")
                    )
                    .append(
                            Component.empty()
                                    .color(NamedTextColor.DARK_GRAY)
                                    .content(Auctioneer.econ.format(this.auction.getTopBidPrice()))
                                    .decorate(TextDecoration.BOLD)
                    )
            );

            onlineBidderPlayer.getInventory().addItem(this.auction.getOfferedItem());

            Player onlineAuthorPlayer = Bukkit.getPlayer(this.auction.getAuthorName());
            OfflinePlayer offlineAuthorPlayer = Bukkit.getOfflinePlayer(this.auction.getAuthorName());

            Auctioneer.econ.depositPlayer(offlineAuthorPlayer, this.auction.getTopBidPrice());

            if (onlineAuthorPlayer != null) {
                onlineAuthorPlayer.sendMessage(Component.empty()
                        .color(NamedTextColor.GREEN)
                        .content("You have received " + Auctioneer.econ.format(this.auction.getTopBidPrice()) + " from your auction!")
                );
            }



        }

        this.gui.closeForAllViewers();
    }
}
