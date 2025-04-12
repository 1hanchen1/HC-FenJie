package com.hanchen.hcfenjie.data.reward

import org.bukkit.entity.Player

/**
 * 奖励接口
 * 定义了奖励的基本方法
 */
interface Reward {
    /**
     * 执行奖励逻辑
     * @param player 玩家对象
     * @param str 奖励参数
     */
    fun exeReward(player: Player, str: String)
}