package com.anotherpillow.auctioneer;

import com.anotherpillow.auctioneer.commands.CommandManager;
import com.anotherpillow.auctioneer.db.DatabaseManager;
import com.anotherpillow.auctioneer.event.EventListeners;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.InvUI;

import java.sql.SQLException;
import java.util.logging.Logger;


public final class Auctioneer extends JavaPlugin {
    @Override
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    public static FileConfiguration config = null;

    public static String version = "1.0.0";

    public Logger logger = getLogger();

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        getServer().getPluginManager().registerEvents(new EventListeners(), this);
        InvUI.getInstance().setPlugin(this);

        CommandManager commandManager = new CommandManager(this);
        try {
            DatabaseManager.connect();
            DatabaseManager.setupTables();
        } catch (SQLException e) {
            logger.warning("Failed to connect to database " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.warning("Failed to setup database " + e.getMessage());
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
