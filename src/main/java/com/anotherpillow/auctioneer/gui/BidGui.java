package com.anotherpillow.auctioneer.gui;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.holder.StartGuiDataHolder;
import com.anotherpillow.auctioneer.item.start.ConfirmItem;
import com.anotherpillow.auctioneer.item.start.IncrementItem;
import com.anotherpillow.auctioneer.item.start.InitialPriceItem;
import com.anotherpillow.auctioneer.item.start.TimeoutItem;
import com.anotherpillow.auctioneer.util.CosmeticItems;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

public class BidGui {

    private Auctioneer plugin = null;
    private Gui gui = null;

    public BidGui(Auctioneer plugin) {
//        ConfigurationSection modes = plugin.getConfig().getConfigurationSection("modes");
        this.plugin = plugin;
        this.gui = this.createGui();
    }

    public Gui createGui() {
        Gui.Builder.Normal builder = Gui.normal() // Creates the GuiBuilder for a normal GUI
                .setStructure(
                        "# # # # # # # # #",
                        "# # # # # # # # #",
                        "# # # # # # # # #",
                        "# # # # # # # # #",
                        "# # # # # # # # #",
                        "# # # # # # # # #")
                .addIngredient('#', new SimpleItem(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("")))
                ;

        return builder.build();
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
