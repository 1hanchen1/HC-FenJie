package com.hanchen.hcfenjie.data.matching.imp

import com.hanchen.hcfenjie.data.matching.Matching
import com.hanchen.hcfenjie.util.LoggerUtil
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
        val result = meta.hasDisplayName() && meta.displayName.equals(args, ignoreCase = true)

        // 调试模式提示
        LoggerUtil.debug("检查物品名称是否相等: 物品=${itemStack.type}, 名称=${meta.displayName}, 目标名称=$args, 结果=$result")

        return result
    }
}