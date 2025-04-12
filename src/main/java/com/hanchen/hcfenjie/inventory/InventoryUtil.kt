package com.hanchen.hcfenjie.inventory

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.util.LoggerUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory

/**
 * 库存工具类
 * 提供分解界面的打开和关闭逻辑
 */
object InventoryUtil {
    const val CONFIRM_SLOT = 49
    private const val DEFAULT_SIZE = 54

    /**
     * 打开分解界面
     * @param player 玩家对象
     */
    fun openInventory(player: Player) {
        val title = Main.instance.inventoryTitle ?: run {
            LoggerUtil.error("库存标题未配置!")
            return
        }
        // 创建库存
        val inventory = Bukkit.createInventory(
            null,
            DEFAULT_SIZE,
            title
        ).apply {
            // 设置确认槽位的物品
            Main.instance.inventoryItemStack?.let { setItem(CONFIRM_SLOT, it) }
        }
        // 打开库存
        player.openInventory(inventory)

        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("为玩家 ${player.name} 打开分解界面")
        }
    }
}