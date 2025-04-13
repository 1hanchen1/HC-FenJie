package com.hanchen.hcfenjie.data.reward

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.util.LoggerUtil
import java.util.concurrent.ConcurrentHashMap

/**
 * 奖励管理器
 * 负责管理奖励类型的注册、获取和清空
 */
object RewardManage {
    private val rewardMap = ConcurrentHashMap<String, Reward>()

    /**
     * 注册奖励类型
     * @param type 奖励类型名称
     * @param reward 奖励类型对象
     */
    fun register(type: String, reward: Reward) {
        rewardMap.computeIfAbsent(type.lowercase()) {
            // 调试模式提示
            LoggerUtil.debug("注册奖励类型: $type")

            LoggerUtil.info("注册奖励类型: $type")
            reward
        } ?: run {
            // 调试模式提示
            LoggerUtil.debug("奖励类型 $type 已存在，跳过重复注册")
            LoggerUtil.warn("奖励类型 $type 已存在，跳过重复注册")
        }
    }

    /**
     * 获取奖励类型
     * @param type 奖励类型名称
     * @return 奖励类型对象或null
     */
    fun getReward(type: String): Reward? {
        return rewardMap[type.lowercase()].also {
            if (it == null) {
                LoggerUtil.warn("未注册的奖励类型: $type")
                // 调试模式提示
                LoggerUtil.debug("尝试获取未注册的奖励类型: $type")
            }
        }
    }

    /**
     * 获取所有已注册的奖励类型的不可变集合
     * @return 包含所有奖励类型名称的不可变集合（如 ["cmd", "item"]）
     */
    fun getAllRewardTypes(): Set<String> = rewardMap.keys.toSet()

    fun clear() {
        rewardMap.clear()
        // 调试模式提示
        LoggerUtil.debug("已清空所有奖励类型")

        LoggerUtil.info("已清空所有奖励类型")
    }
}