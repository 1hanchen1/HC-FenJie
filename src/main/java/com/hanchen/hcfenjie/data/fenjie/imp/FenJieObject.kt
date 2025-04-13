package com.hanchen.hcfenjie.data.fenjie.imp

import com.hanchen.hcfenjie.data.fenjie.FenJie
import com.hanchen.hcfenjie.data.matching.MatchingManage
import com.hanchen.hcfenjie.data.reward.RewardManage
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * 分解对象实现类
 * 负责处理物品分解的逻辑，包括匹配条件和奖励发放
 */
class FenJieObject(
    private var fenJieName: String,
    private var fenJieChange: Double,
    private var fenJieMatchingList: List<String>,
    private var fenJieRewardList: List<String>
) : FenJie {

    /**
     * 默认构造函数
     */
    constructor() : this("", 0.0, emptyList(), emptyList())

    /**
     * 获取分解配置名称
     * @return 分解配置名称
     */
    override fun getFenJieName(): String = fenJieName

    /**
     * 设置匹配条件列表
     * @param matchingList 匹配条件列表
     */
    override fun setFenJieMatching(matchingList: List<String>) {
        fenJieMatchingList = matchingList.toList() // 防御性复制
    }

    /**
     * 获取分解概率
     * @return 分解概率（0.0-1.0）
     */
    override fun getFenJieChange(): Double = fenJieChange

    /**
     * 设置分解概率
     * @param change 分解概率（0.0-1.0）
     */
    override fun setFenJieChange(change: Double) {
        require(change in 0.0..1.0) { "分解概率必须在0-1之间" }
        fenJieChange = change
    }

    /**
     * 设置奖励列表
     * @param rewardList 奖励列表
     */
    override fun setFenJieReward(rewardList: List<String>) {
        fenJieRewardList = rewardList.toList() // 防御性复制
    }

    /**
     * 获取奖励列表
     * @return 奖励列表
     */
    override fun getFenJieReward(): List<String> = fenJieRewardList

    /**
     * 获取匹配条件列表
     * @return 匹配条件列表
     */
    override fun getFenJieMatching(): List<String> = fenJieMatchingList

    /**
     * 执行奖励逻辑
     * @param player 玩家对象
     */
    override fun exeReward(player: Player) {
        // 调试模式提示
        LoggerUtil.debug("执行奖励: 玩家=${player.name}, 分解配置=${fenJieName}")

        fenJieRewardList.forEach { rewardStr ->
            rewardStr.split("<->", limit = 2).takeIf { it.size == 2 }?.let { (type, args) ->
                RewardManage.getReward(type)?.exeReward(player, args) ?: run {
                    // 使用配置的未知奖励类型消息
                    MessageUtil.sendFormattedMessage(
                        player,
                        "rewards.unknown-type", // 配置键
                        "type" to type          // 占位符参数
                    )
                }
            } ?: run {
                // 使用配置的奖励格式错误消息
                MessageUtil.sendFormattedMessage(
                    player,
                    "rewards.invalid-format",   // 配置键
                    "rewardStr" to rewardStr    // 占位符参数
                )
            }
        }
    }

    /**
     * 检查物品是否匹配条件
     * @param itemStack 物品对象
     * @return 是否匹配
     */
    override fun isMatching(itemStack: ItemStack): Boolean {
        // 调试模式提示
        LoggerUtil.debug("检查物品匹配: 物品=${itemStack.type}, 分解配置=${fenJieName}")

        return fenJieMatchingList.any { matchingStr ->
            matchingStr.split("<->", limit = 2).takeIf { it.size == 2 }?.let { (type, args) ->
                MatchingManage.getMatching(type)?.isMatching(itemStack, args) ?: false
            } ?: false
        }
    }
}