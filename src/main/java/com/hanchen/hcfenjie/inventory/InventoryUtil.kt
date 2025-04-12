package com.hanchen.hcfenjie.inventory

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.util.LoggerUtil
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

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

        // 调试模式提示 i
        LoggerUtil.debug("为玩家 ${player.name} 打开分解界面")

    }

    // 添加物品给予方法（处理背包满和掉落逻辑）
    fun giveItemSafely(player: Player, item: ItemStack): Int {
        val initialAmount = item.amount
        val result = player.inventory.addItem(item)
        val remaining = result.values.sumOf { it?.amount ?: 0 }

        // 掉落剩余物品
        if (remaining > 0) {
            val world = player.world
            val location = player.location
            val dropItem = item.clone().apply { this.amount = remaining }
            world.dropItem(location, dropItem)
        }

        return remaining
    }
}