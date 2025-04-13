package com.hanchen.hcfenjie.data.fenjie

import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.RewardParser
import org.bukkit.entity.Player

/**
 * 分解管理器
 * 负责管理分解配置的注册、获取和清空
 */
object FenJieManage {
    private val fenJieMap = mutableMapOf<String, FenJie>()

    /**
     * 注册分解配置
     * @param fenJieName 分解配置名称
     * @param fenJie 分解配置对象
     */
    fun register(fenJieName: String, fenJie: FenJie) {
        fenJieMap.computeIfAbsent(fenJieName) {
            LoggerUtil.info("注册分解配置: $fenJieName")
            fenJie
        } ?: run {
            // 调试模式提示
            LoggerUtil.debug("分解配置 $fenJieName 已存在，跳过注册")
            LoggerUtil.warn("分解配置 $fenJieName 已存在，跳过注册")
        }
    }

    /**
     * 获取所有分解配置
     * @return 分解配置映射
     */
    fun getFenJieMap(): Map<String, FenJie> = fenJieMap.toMap()

    /**
     * 清空所有分解配置
     */
    fun clear() {
        fenJieMap.clear()
        LoggerUtil.info("已清空所有分解配置")
    }

    // 修改奖励执行部分
    private fun executeReward(player: Player, reward: String) {
        val args = reward.split("<->")
        if (args.size < 2) {
            LoggerUtil.error("无效的奖励格式: $reward")
            return
        }

        val type = args[0]
        val rewardArgs = args.drop(1)

        when (type.uppercase()) {
            "CMD" -> handleCommand(player, rewardArgs)
            "MM" -> handleMythicMobs(player, rewardArgs)
            "NI" -> handleNeigeItems(player, rewardArgs)
            "ECO" -> handleEconomy(player, rewardArgs)
            else -> LoggerUtil.error("未知奖励类型: $type")
        }
    }

    // 新增处理逻辑（示例处理NI类型）
    private fun handleNeigeItems(player: Player, args: List<String>) {
        if (args.size < 2) {
            LoggerUtil.error("NI奖励参数不足: ${args.joinToString()}")
            return
        }

        val itemName = args[0]
        val quantity = RewardParser.parseQuantity(args[1])

        try {
            val item = NeigeItemsAPI.getItemStack(player, itemName, quantity)
            player.inventory.addItem(item)
            LoggerUtil.debug("发放NI物品: $itemName x$quantity")
        } catch (e: Exception) {
            LoggerUtil.error("NI物品发放失败: ${e.message}")
        }
    }
}