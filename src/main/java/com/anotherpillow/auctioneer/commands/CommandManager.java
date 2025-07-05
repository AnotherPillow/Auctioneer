package com.anotherpillow.auctioneer.commands;

import com.anotherpillow.auctioneer.Auctioneer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager {
    public Map<String, CommandExecutor> commands = new HashMap<>();

    public CommandManager(Auctioneer plugin) {
        this.commands.put("auctioneer", new InfoCommand(plugin));
        this.commands.put("increment", new IncrementCommand(plugin));

        this.commands.put("startbidding", new StartCommand(plugin));
        this.commands.put("startbids", new StartCommand(plugin));
        this.commands.put("upauction", new StartCommand(plugin));

        this.commands.put("viewauction", new ViewAuctionCommand(plugin));
        this.commands.put("openauction", new ViewAuctionCommand(plugin));
        this.commands.put("bid", new ViewAuctionCommand(plugin));

        this.RegisterPluginCommands(plugin);
    }

    public void RegisterPluginCommands(Auctioneer plugin) {
        for (Map.Entry<String, CommandExecutor> entry : this.commands.entrySet()) {
            PluginCommand command = plugin.getCommand(entry.getKey());
            if (command != null) command.setExecutor(entry.getValue());
            else plugin.logger.warning("Cannot define command " + entry.getKey() + ", is it defined in the plugin.yml?");
        }
    }
}
