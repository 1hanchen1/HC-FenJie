package com.hanchen.hcfenjie.listener

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.data.fenjie.FenJieManage
import com.hanchen.hcfenjie.inventory.InventoryUtil
import com.hanchen.hcfenjie.util.ChangeUtil
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory

/**
 * 库存点击监听器
 * 处理玩家在分解界面中的点击事件
 */
class InventoryClickListener : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory = event.clickedInventory ?: return
        val title = Main.instance.inventoryTitle ?: return

        // 检查库存标题是否匹配
        if (inventory.title != title) return

        // 取消事件默认行为
        event.isCancelled = true

        // 检查是否点击确认槽位
        if (event.slot != InventoryUtil.CONFIRM_SLOT) return

        // 获取玩家对象
        val player = event.whoClicked as? Player ?: return

        // 记录处理开始时间
        val startTime = System.currentTimeMillis()

        // 处理物品分解
        processItems(inventory, player)

        // 记录处理耗时
        LoggerUtil.debug("物品处理耗时: ${System.currentTimeMillis() - startTime}ms")
    }

    /**
     * 处理物品分解
     * @param inventory 库存对象
     * @param player 玩家对象
     */
    private fun processItems(inventory: Inventory, player: Player) {
        var successCount = 0
        var failedCount = 0

        // 遍历库存中的物品
        inventory.contents.forEachIndexed { index, itemStack ->
            if (index == InventoryUtil.CONFIRM_SLOT || itemStack == null) return@forEachIndexed

            // 检查物品是否匹配分解条件
            FenJieManage.getFenJieMap().values
                .firstOrNull { it.isMatching(itemStack) }
                ?.let { fenJie ->
                    repeat(itemStack.amount) {
                        // 检查分解概率
                        if (ChangeUtil.checkProbability(fenJie.getFenJieChange())) {
                            successCount++
                            // 执行奖励逻辑
                            runCatching { fenJie.exeReward(player) }
                                .onFailure { e ->
                                    LoggerUtil.error("奖励执行失败: ${e.message}")
                                    player.sendMessage("§c部分奖励发放失败")
                                }
                        } else {
                            failedCount++
                        }
                    }
                    // 移除分解的物品
                    inventory.setItem(index, null)
                }
        }

        // 发送分解结果消息
        sendResultMessage(player, successCount, failedCount)
    }

    /**
     * 发送分解结果消息
     * @param player 玩家对象
     * @param success 成功分解的物品数量
     * @param failed 失败分解的物品数量
     */
    private fun sendResultMessage(player: Player, success: Int, failed: Int) {
        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("分解结果: 玩家=${player.name}, 成功=$success, 失败=$failed")
        }

        Main.instance.apply {
            successMessage?.takeIf { it != "none" }?.let {
                val formattedMessage = it.replace("<number>", success.toString())
                MessageUtil.sendMessage(player, formattedMessage, true)
            }
            failedMessage?.takeIf { it != "none" }?.let {
                val formattedMessage = it.replace("<number>", failed.toString())
                MessageUtil.sendMessage(player, formattedMessage, true)
            }
        }
    }
}