package com.hanchen.hcfenjie.data.matching.imp

import com.hanchen.hcfenjie.data.matching.Matching
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.inventory.ItemStack

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
        // 新增颜色代码转换
        val processedArgs = MessageUtil.translateAdvancedColorCodes(args)

        return meta.lore?.any { loreLine ->
            loreLine.contains(processedArgs, ignoreCase = true)
        } ?: false.also { result ->
            LoggerUtil.debug("ContainsLore匹配: 物品=${itemStack.type} 目标=${processedArgs} 结果=$result")
        }
    }
}