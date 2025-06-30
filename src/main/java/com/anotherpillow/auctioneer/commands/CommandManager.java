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
        this.commands.put("auctioneer", new InfoCommand());

        this.RegisterPluginCommands(plugin);
    }

    public void RegisterPluginCommands(SkyblockGauntlets plugin) {
        for (Map.Entry<String, CommandExecutor> entry : this.commands.entrySet()) {
            PluginCommand command = plugin.getCommand(entry.getKey());
            if (command != null) command.setExecutor(entry.getValue());
            else plugin.logger.warning("Cannot define command " + entry.getKey() + ", is it defined in the plugin.yml?");
        }
    }
}
