package com.hanchen.hcfenjie.data.matching

import org.bukkit.inventory.ItemStack

/**
 * 物品匹配条件接口
 * @param itemStack 待检查的物品
 * @param args 匹配参数（根据具体实现解析）
 */
interface Matching {
    /**
     * 检查物品是否匹配条件
     * @param itemStack 物品对象
     * @param args 匹配参数（根据具体实现解析）
     * @return 是否匹配
     */
    fun isMatching(itemStack: ItemStack, args: String): Boolean
}