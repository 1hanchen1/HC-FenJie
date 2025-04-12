package com.hanchen.hcfenjie.listener

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.inventory.InventoryUtil
import com.hanchen.hcfenjie.util.LoggerUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack

/**
 * 库存关闭监听器
 * 处理玩家关闭分解界面时的逻辑
 */
class InventoryCloseListener : Listener {
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventory = event.inventory
        val plugin = Main.instance ?: return
        val expectedTitle = plugin.inventoryTitle ?: return

        // 安全类型转换和空值检查
        val player = event.player as? Player ?: return
        if (inventory.title != expectedTitle) return

        // 收集需要返还的物品（排除确认槽位）
        val itemsToReturn = inventory.contents
            .withIndex()
            .filter { (index, item) ->
                index != InventoryUtil.CONFIRM_SLOT &&
                        item != null &&
                        item.type != org.bukkit.Material.AIR
            }
            .map { it.value }

        // 返还物品到玩家背包，处理掉落逻辑
        itemsToReturn.forEach { itemStack ->
            val remaining = player.inventory.addItem(itemStack).values
            if (remaining.isNotEmpty()) {
                remaining.forEach { leftover ->
                    player.world.dropItem(player.location, leftover).apply {
                        // 调试模式提示
                        if (Main.instance.config.getBoolean("debug-mode", false)) {
                            LoggerUtil.debug("玩家 ${player.name} 背包已满，物品掉落在地面: ${leftover.type}")
                        }
                    }
                }
            }
            inventory.removeItem(itemStack) // 清空原库存物品
        }

        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("玩家 ${player.name} 关闭分解界面，返还 ${itemsToReturn.size} 个物品")
        }
    }
}