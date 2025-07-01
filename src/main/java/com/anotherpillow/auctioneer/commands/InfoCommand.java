package com.anotherpillow.auctioneer.commands;

import com.anotherpillow.auctioneer.Auctioneer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class InfoCommand implements CommandExecutor {
    public Auctioneer plugin = null;

    public InfoCommand(Auctioneer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final Component response = Component.text(String.format("Auctioneer v%s (Public)", Auctioneer.version))
                .color(NamedTextColor.DARK_PURPLE)
                .append(Component.text("\nAuthor: ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("AnotherPillow (https://pillow.rocks)").color(TextColor.color(0xE377FF)))
                .append(Component.text("\nSource: ").color(NamedTextColor.LIGHT_PURPLE))
                .append(Component.text("https://github.com/AnotherPillow/Auctioneer").color(TextColor.color(0xE377FF)));
        sender.sendMessage(response);

        return true;
    }
}
