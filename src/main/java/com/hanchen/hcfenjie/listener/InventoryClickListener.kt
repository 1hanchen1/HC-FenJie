package com.hanchen.hcfenjie.listener

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.data.fenjie.FenJieManage
import com.hanchen.hcfenjie.util.ChangeUtil
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory

class InventoryClickListener : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.inventory
        val title = Main.instance.inventoryTitle ?: return

        // 仅处理分解界面
        if (inventory.type != InventoryType.CHEST || inventory.title != title) return

        val player = event.whoClicked as? Player ?: return

        when {
            // 允许玩家在自己的背包和分解槽位之间拖动物品
            event.clickedInventory == player.inventory -> {
                LoggerUtil.debug("玩家 ${player.name} 操作背包槽位: ${event.slot}")
            }

            // 处理分解槽位点击
            event.clickedInventory == inventory && event.slot != 49 -> {
                handleDecompSlotInteraction(event, player)
            }

            // 点击确认按钮时触发分解
            event.slot == 49 -> {
                event.isCancelled = true
                LoggerUtil.debug("玩家 ${player.name} 点击确认按钮")
                processDecomposition(player, inventory) // 新增分解逻辑
            }
        }
    }

    /**
     * 处理分解槽位物品放置逻辑
     */
    private fun handleDecompSlotInteraction(event: InventoryClickEvent, player: Player) {
        val item = event.currentItem
        if (item != null && item.type != Material.AIR) {
            val canPlace = FenJieManage.getFenJieMap().values.any { it.isMatching(item) }
            event.isCancelled = !canPlace
            if (!canPlace) {
                player.updateInventory()
                MessageUtil.sendFormattedMessage(
                    player,
                    "decomposition.invalid-item",
                    "item" to (item.type ?: "未知物品") // 直接使用枚举转换
                )
            }
        }
    }

    /**
     * 执行分解核心逻辑
     */
    private fun processDecomposition(player: Player, inventory: Inventory) {
        var successCount = 0
        var failedCount = 0

        // 遍历所有分解槽位（排除确认按钮槽位49）
        for (slot in 0 until inventory.size) {
            if (slot == 49) continue

            val itemStack = inventory.getItem(slot) ?: continue
            if (itemStack.type == Material.AIR) continue

            // 查找匹配的分解配置
            FenJieManage.getFenJieMap().values.firstOrNull { it.isMatching(itemStack) }?.let { fenJie ->
                repeat(itemStack.amount) {
                    if (ChangeUtil.checkProbability(fenJie.getFenJieChange())) {
                        successCount++
                        try {
                            fenJie.exeReward(player)
                        } catch (e: Exception) {
                            LoggerUtil.error("奖励执行异常: ${e.stackTraceToString()}")
                        }
                    } else {
                        failedCount++
                    }
                }
                inventory.setItem(slot, null) // 清空槽位
            }
        }

        // 新增：计算动态颜色
        val total = successCount + failedCount
        val successRate = if (total > 0) successCount.toDouble() / total else 0.0
        // 动态颜色逻辑（根据成功率）
        val rateColor = when {
            successRate >= 0.7 -> "&a"
            successRate >= 0.3 -> "&e"
            else -> "&c"
        }

        // 合并消息发送
        MessageUtil.sendFormattedMessage(
            player,
            "decomposition.result",
            "success" to successCount,
            "failed" to failedCount,
            "rate" to "%.1f%%".format(successRate * 100),
            "rate_color" to rateColor // 传递动态颜色
        )

        LoggerUtil.debug("分解完成 | 成功: $successCount 次 | 失败: $failedCount 次")
    }
}