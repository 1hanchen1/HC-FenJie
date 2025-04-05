package com.hanchen.hcfenjie.inventory

import com.hanchen.hcfenjie.Main
import org.bukkit.Bukkit
import org.bukkit.entity.Player

object InventoryUtil {
    fun openInventory(player: Player) {
        val inventory = Bukkit.createInventory(null, 54, Main.instance.inventoryTitle!!)
        inventory.setItem(49, Main.instance.inventoryItemStack)
        player.openInventory(inventory)
    }
}