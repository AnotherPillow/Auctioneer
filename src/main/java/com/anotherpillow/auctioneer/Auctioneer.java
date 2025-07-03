package com.anotherpillow.auctioneer;

import com.anotherpillow.auctioneer.commands.CommandManager;
import com.anotherpillow.auctioneer.db.DatabaseManager;
import com.anotherpillow.auctioneer.event.EventListeners;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.InvUI;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;

import java.sql.SQLException;
import java.util.logging.Logger;


public final class Auctioneer extends JavaPlugin {
    @Override
    public @NotNull Logger getLogger() {
        return super.getLogger();
    }

    public static String version = "1.0.0";

    public Logger logger = getLogger();

    public static FileConfiguration config = null;
    public static Economy econ = null;

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

        if (!setupEconomy() ) {
            logger.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
