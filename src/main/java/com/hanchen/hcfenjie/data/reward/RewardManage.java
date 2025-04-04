package com.hanchen.hcfenjie.data.reward;

import java.util.HashMap;
import java.util.Map;

public class RewardManage {
    private static final Map<String, Reward> rewardMap = new HashMap();

    public static void register(String type, Reward reward) {
        rewardMap.putIfAbsent(type, reward);
        System.out.println("[EP-FenJie]§a成功注册奖励类型: " + type);
    }

    public static Map<String, Reward> getRewardMap() {
        return rewardMap;
    }

    public static Reward getReward(String type) {
        if (rewardMap.get(type) != null) {
            return rewardMap.get(type);
        }
        System.out.println("[EP-FenJie]§c奖励不存在 类型: " + type);
        return null;
    }
}