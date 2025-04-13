package com.hanchen.hcfenjie.data.matching.imp

import com.hanchen.hcfenjie.data.matching.Matching
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.inventory.ItemStack

/**
 * 名称包含匹配器
 * 检测物品显示名称是否包含指定字符串（不区分大小写）
 */
class ContainsName : Matching {
    override fun isMatching(itemStack: ItemStack, args: String): Boolean {
        val meta = itemStack.itemMeta ?: return false
        // 新增颜色代码转换
        val processedArgs = MessageUtil.translateAdvancedColorCodes(args)
        val displayName = meta.displayName ?: return false

        return displayName.contains(processedArgs, ignoreCase = true).also { result ->
            LoggerUtil.debug("ContainsName匹配: 物品名=${MessageUtil.translateAdvancedColorCodes(displayName)} 目标=$processedArgs 结果=$result")
        }
    }
}