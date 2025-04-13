package com.hanchen.hcfenjie.data.matching.imp

import com.hanchen.hcfenjie.data.matching.Matching
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.inventory.ItemStack

/**
 * Lore完全匹配器
 * 检测物品Lore与指定Lore完全一致（区分颜色代码）
 */
class EqualsLore : Matching {
    override fun isMatching(itemStack: ItemStack, args: String): Boolean {
        val meta = itemStack.itemMeta ?: return false
        // 提前转换颜色代码
        val configLore = args.split("\\n").map {
            MessageUtil.translateAdvancedColorCodes(it)
        }

        val itemLore = meta.lore?.map {
            MessageUtil.translateAdvancedColorCodes(it)
        } ?: return configLore.isEmpty()

        return itemLore == configLore
    }
}