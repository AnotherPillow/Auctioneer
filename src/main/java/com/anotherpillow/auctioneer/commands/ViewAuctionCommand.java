package com.anotherpillow.auctioneer.commands;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.AuctionManager;
import com.anotherpillow.auctioneer.gui.StartGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.window.Window;

public class ViewAuctionCommand implements CommandExecutor {
    public Auctioneer plugin = null;

    public ViewAuctionCommand(Auctioneer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            sender.sendMessage(Component.empty().content("Invalid arguments").color(NamedTextColor.DARK_RED));
            return true;
        }
        //  + args[0]
        sender.sendMessage(Component.empty().content("Opening auction... ").color(NamedTextColor.GREEN));

        String uuid = args[0];

        if (!AuctionManager.auctionExists(uuid)) {
            sender.sendMessage(Component.empty().content("Auction does not exist!").color(NamedTextColor.RED));
            return true;
        }

        return true;
    }
}

