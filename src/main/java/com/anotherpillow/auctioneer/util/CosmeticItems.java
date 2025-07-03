package com.anotherpillow.auctioneer.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CosmeticItems {

    public static ItemStack getBalanceNugget(double balance) {
        String name = ChatColor.translateAlternateColorCodes(
                '&',
                "&fCurrent balance: &6$" + balance
        );
        ItemStack nugget = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta meta = nugget.getItemMeta();
        meta.setDisplayName(name);
        nugget.setItemMeta(meta);

        return nugget;
    }
}
