package com.anotherpillow.auctioneer.commands;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.gui.ListingsGui;
import com.anotherpillow.auctioneer.gui.StartGui;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ListingsCommand implements CommandExecutor {
    public Auctioneer plugin = null;

    public ListingsCommand(Auctioneer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command.");
            return true;
        }

        ListingsGui gui = new ListingsGui(this.plugin);
        gui.render(player);

        return true;
    }
}
