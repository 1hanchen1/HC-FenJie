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
        val itemLore = meta.lore ?: return args.isBlank() // 当配置Lore为空时匹配无Lore物品

        // 分割配置中的Lore（按行分割）
        val configLore = args.split("\\n").map { MessageUtil.translateAdvancedColorCodes(it) }

        return itemLore == configLore.also { result ->
            val logMsg = buildString {
                append("EqualsLore匹配结果: $result")
                append("\n配置Lore:")
                configLore.forEach { append("\n  - '$it'") }
                append("\n物品Lore:")
                itemLore.forEach { append("\n  - '$it'") }
            }
            LoggerUtil.debug(logMsg)
        }
    }
}