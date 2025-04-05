package com.hanchen.hcfenjie.listener

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.data.fenjie.FenJieManage
import com.hanchen.hcfenjie.util.ChangeUtil
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class InventoryClickListener : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.clickedInventory ?: return
        if (inventory.title != Main.instance.inventoryTitle) return

        val player = event.whoClicked as Player
        if (event.slot == 49) {
            event.isCancelled = true
            var yes = 0
            var no = 0

            for (i in 0 until inventory.size) {
                if (i != 49) {
                    val itemStack = inventory.getItem(i)
                    if (itemStack != null && itemStack.type != Material.AIR && itemStack.hasItemMeta()) {
                        for (fenJie in FenJieManage.getFenJieMap().values) {
                            if (fenJie.isMatching(itemStack)) {
                                for (a in 1..itemStack.amount) {
                                    if (ChangeUtil.check(fenJie.getFenJieChange())) {
                                        yes++
                                        fenJie.exeReward(player)
                                    } else {
                                        no++
                                    }
                                }
                                inventory.setItem(i, ItemStack(Material.AIR))
                            }
                        }
                    }
                }
            }

            if (Main.instance.successMessage != "none") {
                player.sendMessage(Main.instance.successMessage!!.replace("<number>", yes.toString()))
            }
            if (Main.instance.failedMessage != "none") {
                player.sendMessage(Main.instance.failedMessage!!.replace("<number>", no.toString()))
            }
        }
    }
}