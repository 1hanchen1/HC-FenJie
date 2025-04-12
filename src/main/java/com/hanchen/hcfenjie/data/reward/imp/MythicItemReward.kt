package com.hanchen.hcfenjie.data.reward.imp

import com.hanchen.hcfenjie.data.reward.Reward
import com.hanchen.hcfenjie.inventory.InventoryUtil
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import io.lumine.xikage.mythicmobs.MythicMobs
import org.bukkit.entity.Player

class MythicItemReward : Reward {
    companion object {
        private const val PREFIX = "mm<->"
        private const val DELIMITER = "<->"
    }

    override fun exeReward(player: Player, exeString: String) {
        // 1. 分割参数
        val parts = exeString.split(DELIMITER)
        if (parts.size < 1) {
            LoggerUtil.warn("MythicMobs物品参数不完整: $exeString")
            return
        }

        // 2. 解析物品ID和数量
        val itemName = parts[0]
        val amountStr = parts.getOrNull(1) ?: "1"
        val amount = amountStr.toIntOrNull()?.coerceIn(1, 64) ?: 1.also {
            LoggerUtil.warn("无效的数量参数: $amountStr，默认使用1")
        }

        // 3. 获取MythicMobs物品
        val mythicItem = MythicMobs.inst().itemManager.getItemStack(itemName)?.apply {
            this.amount = amount
        } ?: run {
            LoggerUtil.error("MythicMobs物品不存在: $itemName")
            player.sendMessage("§c错误：物品 $itemName 不存在")
            return
        }

        // 4. 给予玩家物品
        val remaining = InventoryUtil.giveItemSafely(player, mythicItem)
        if (remaining > 0) {
            player.world.dropItem(player.location, mythicItem.apply { this.amount = remaining })
            MessageUtil.sendMessage(player,"§e背包已满，${remaining}个物品掉落在地")
        }

        LoggerUtil.debug("玩家 ${player.name} 获得物品 | 物品: $itemName x${amount - remaining}")
    }
}