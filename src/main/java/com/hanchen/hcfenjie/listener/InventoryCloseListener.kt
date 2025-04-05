package com.hanchen.hcfenjie.listener

import com.hanchen.hcfenjie.Main
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

class InventoryCloseListener : Listener {
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventory = event.inventory
        if (inventory != null && inventory.title == Main.instance.inventoryTitle) {
            val player = event.player as Player
            val itemStackList = ArrayList<ItemStack>()
            for (i in 0 until inventory.size) {
                if (i != 49) {
                    val itemStack = inventory.getItem(i)
                    if (itemStack != null && itemStack.type != Material.AIR) {
                        itemStackList.add(itemStack)
                    }
                }
            }
            for (itemStack in itemStackList) {
                player.inventory.addItem(itemStack)
            }
        }
    }
}