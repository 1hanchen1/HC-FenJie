package com.hanchen.hcfenjie.data.matching.imp

import com.hanchen.hcfenjie.data.matching.Matching
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.inventory.ItemStack

/**
 * 名称相等匹配器
 * 检查物品的名称是否与指定文本相等
 */
class EqualsName : Matching {
    /**
     * 检查物品是否匹配条件
     * @param itemStack 物品对象
     * @param args 匹配参数（要检查的名称）
     * @return 是否匹配
     */
    override fun isMatching(itemStack: ItemStack, args: String): Boolean {
        val meta = itemStack.itemMeta ?: return false
        // 转换颜色代码后比较
        val processedArgs = MessageUtil.translateAdvancedColorCodes(args)
        val itemName = MessageUtil.translateAdvancedColorCodes(meta.displayName ?: "")

        return itemName.equals(processedArgs, ignoreCase = true).also { result ->
            LoggerUtil.debug("EqualsName匹配: 物品名=$itemName 目标=$processedArgs 结果=$result")
        }
    }
}