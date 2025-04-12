package com.hanchen.hcfenjie.data.matching.imp

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.data.matching.Matching
import com.hanchen.hcfenjie.util.LoggerUtil
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * 包含描述匹配器
 * 检查物品的描述是否包含指定文本
 */
class ContainsLore : Matching {
    /**
     * 检查物品是否匹配条件
     * @param itemStack 物品对象
     * @param args 匹配参数（要检查的文本）
     * @return 是否匹配
     */
    override fun isMatching(itemStack: ItemStack, args: String): Boolean {
        val meta = itemStack.itemMeta ?: return false
        val result = meta.lore?.any { loreLine ->
            loreLine.contains(args, ignoreCase = true)
        } ?: false

        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("检查物品描述是否包含: 物品=${itemStack.type}, 文本=$args, 结果=$result")
        }

        return result
    }
}