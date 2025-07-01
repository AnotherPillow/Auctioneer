package com.anotherpillow.auctioneer.commands;

import com.anotherpillow.auctioneer.Auctioneer;
import com.anotherpillow.auctioneer.db.DatabaseManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class IncrementCommand implements CommandExecutor  {
    public Auctioneer plugin = null;

    public IncrementCommand(Auctioneer plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players may use this command.");
            return true;
        }

        Player player = (Player) sender;
        String uuid = player.getUniqueId().toString();

        try {
            String selectSql = "SELECT count FROM player_counters WHERE uuid = ?";
            try (
                PreparedStatement selStmt = DatabaseManager.connection.prepareStatement(selectSql)
            ) {
                selStmt.setString(1, uuid);
                try (ResultSet rs = selStmt.executeQuery()) {
                    int newCount;
                    if (rs.next()) {
                        newCount = rs.getInt("count") + 1;
                        String updateSql = "UPDATE player_counters SET count = ? WHERE uuid = ?";
                        try (
                            PreparedStatement updateStmt = DatabaseManager.connection.prepareStatement(updateSql)
                        ) {
                            updateStmt.setInt(1, newCount);
                            updateStmt.setString(2, uuid);
                            updateStmt.executeUpdate();
                        }
                    } else {
                        newCount = 1;
                        String insertSql = "INSERT INTO player_counters (uuid, count) VALUES (?, ?)";
                        try (
                            PreparedStatement insertStmt = DatabaseManager.connection.prepareStatement(insertSql)
                        ) {
                            insertStmt.setString(1, uuid);
                            insertStmt.setInt(2, newCount);
                            insertStmt.executeUpdate();
                        }
                    }
                    player.sendMessage(
                        "Your new count is: " + newCount
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage("An error occurred. See console.");
        }

        return true;
    }
}
