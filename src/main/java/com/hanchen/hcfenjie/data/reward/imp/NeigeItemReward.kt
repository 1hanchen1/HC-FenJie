package com.hanchen.hcfenjie.data.reward.imp

import com.hanchen.hcfenjie.data.reward.Reward
import com.hanchen.hcfenjie.inventory.InventoryUtil
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.entity.Player
import pers.neige.neigeitems.manager.ItemManager

class NeigeItemReward : Reward {
    companion object {
        private const val PREFIX = "neigeitem<->"
        private const val DELIMITER = "<->"
    }

    override fun exeReward(player: Player, exeString: String) {
        // 1. 分割参数
        val args = exeString.split(DELIMITER)
        if (args.size < 1) {
            LoggerUtil.warn("NeigeItems参数不完整: $exeString")
            return
        }

        // 2. 解析物品ID和数量
        val itemId = args[0]
        val amountStr = args.getOrNull(1) ?: "1"
        val amount = amountStr.toIntOrNull()?.coerceIn(1, 64) ?: 1.also {
            LoggerUtil.warn("无效的数量参数: $amountStr，默认使用1")
        }

        // 3. 获取NeigeItems物品
        val itemStack = try {
            ItemManager.getItemStack(itemId, player)?.apply {
                this.amount = amount
            }
        } catch (e: Exception) {
            LoggerUtil.error("获取NeigeItems物品失败: ${e.stackTraceToString()}")
            null
        } ?: run {
            MessageUtil.sendMessage(player, "§c错误：物品 $itemId 不存在")
            return
        }

        // 4. 给予玩家物品
        val remaining = InventoryUtil.giveItemSafely(player, itemStack)
        if (remaining > 0) {
            player.world.dropItem(player.location, itemStack.apply {
                this.amount = remaining
            })
            MessageUtil.sendMessage(player, "§e背包已满，${remaining}个物品掉落在地")
        }

        LoggerUtil.debug("玩家 ${player.name} 获得Neige物品 | ID: $itemId x${amount - remaining}")
    }
}