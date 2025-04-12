package com.hanchen.hcfenjie.data.fenjie

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 分解功能核心接口
 * @property FenJieName 分解配置名称
 * @property FenJieChange 分解成功概率（0.0-1.0）
 */
interface FenJie {
    /**
     * 获取分解配置名称
     * @return 分解配置名称
     */
    fun getFenJieName(): String

    /**
     * 获取分解成功概率
     * @return 分解成功概率（0.0-1.0）
     */
    fun getFenJieChange(): Double

    /**
     * 设置分解成功概率
     * @param change 分解成功概率（0.0-1.0）
     */
    fun setFenJieChange(change: Double)

    /**
     * 获取奖励列表
     * @return 奖励列表
     */
    fun getFenJieReward(): List<String>

    /**
     * 设置奖励列表
     * @param rewardList 奖励列表
     */
    fun setFenJieReward(rewardList: List<String>)

    /**
     * 获取匹配条件列表
     * @return 匹配条件列表
     */
    fun getFenJieMatching(): List<String>

    /**
     * 设置匹配条件列表
     * @param matchingList 匹配条件列表
     */
    fun setFenJieMatching(matchingList: List<String>)

    /**
     * 执行奖励逻辑
     * @param player 玩家对象
     */
    fun exeReward(player: Player)

    /**
     * 检查物品是否匹配条件
     * @param itemStack 物品对象
     * @return 是否匹配
     */
    fun isMatching(itemStack: ItemStack): Boolean
}