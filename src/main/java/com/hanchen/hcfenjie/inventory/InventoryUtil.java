package com.hanchen.hcfenjie.inventory;

import com.hanchen.hcfenjie.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


public class InventoryUtil {
    public static void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory((InventoryHolder) null, 54, Main.getInstance().inventoryTitle);
        inventory.setItem(49, Main.getInstance().inventoryItemStack);
        player.openInventory(inventory);
    }
}
