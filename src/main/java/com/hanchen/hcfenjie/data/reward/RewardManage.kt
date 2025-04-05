package com.hanchen.hcfenjie.data.reward

object RewardManage {
    private val rewardMap = HashMap<String, Reward>()

    fun register(type: String, reward: Reward) {
        if (!rewardMap.containsKey(type)) {
            rewardMap[type] = reward
            println("[HC-FenJie]§a成功注册奖励类型: $type")
        }
    }

    fun getRewardMap(): MutableMap<String, Reward> {
        return rewardMap
    }

    fun getReward(type: String): Reward? {
        return rewardMap[type].also {
            if (it == null) {
                println("[HC-FenJie]§c奖励不存在 类型: $type")
            }
        }
    }

    fun clear() {
        rewardMap.clear()
    }
}