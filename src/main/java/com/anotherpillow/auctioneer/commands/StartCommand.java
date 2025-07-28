/**
 * Auctioneer source code Copyright (c) AnotherPillow 2025. All Rights Reserved
 * See `License.md` in the root of this repository for full details.
 */

package com.anotherpillow.auctioneer.commands;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.gui.StartGui;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StartCommand implements CommandExecutor  {
    public Auctioneer plugin = null;

    public StartCommand(Auctioneer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players may use this command.");
            return true;
        }

        StartGui gui = new StartGui(this.plugin);
        gui.render(player);


//        final Component response = Component.text(String.format("Auctioneer v%s (Public)", Auctioneer.version))
//                .color(NamedTextColor.DARK_PURPLE)
//                .append(Component.text("\nAuthor: ").color(NamedTextColor.LIGHT_PURPLE))
//                .append(Component.text("AnotherPillow (https://pillow.rocks)").color(TextColor.color(0xE377FF)))
//                .append(Component.text("\nSource: ").color(NamedTextColor.LIGHT_PURPLE))
//                .append(Component.text("https://github.com/AnotherPillow/Auctioneer").color(TextColor.color(0xE377FF)));
//        sender.sendMessage(response);

        return true;
    }
}
