package com.hanchen.hcfenjie.data.fenjie.imp

import com.hanchen.hcfenjie.data.fenjie.FenJie
import com.hanchen.hcfenjie.data.matching.MatchingManage
import com.hanchen.hcfenjie.data.reward.RewardManage
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class FenJieObject(
    private var fenJieName: String,
    private var fenJieChange: Double,
    private var fenJieMatchingList: List<String>,
    private var fenJieRewardList: List<String>
) : FenJie {

    constructor() : this("", 0.0, emptyList(), emptyList())

    override fun getFenJieName(): String {
        return fenJieName
    }

    override fun setFenJieMatching(matchingList: List<String>) {
        fenJieMatchingList = matchingList
    }

    override fun getFenJieChange(): Double {
        return fenJieChange
    }

    override fun setFenJieChange(change: Double) {
        fenJieChange = change
    }

    override fun setFenJieReward(rewardList: List<String>) {
        fenJieRewardList = rewardList
    }

    override fun getFenJieReward(): List<String> {
        return fenJieRewardList
    }

    override fun getFenJieMatching(): List<String> {
        return fenJieMatchingList
    }

    override fun exeReward(player: Player) {
        for (exeReward in fenJieRewardList) {
            val rewardType = exeReward.split("<->")[0]
            val reward = RewardManage.getReward(rewardType)
            if (reward != null) {
                reward.exeReward(player, exeReward)
            }
        }
    }

    override fun isMatching(itemStack: ItemStack): Boolean {
        var match = false
        for (matchingString in fenJieMatchingList) {
            val matchingType = matchingString.split("<->")[0]
            val matching = MatchingManage.getMatching(matchingType)
            if (matching != null) {
                match = matching.isMatching(itemStack, matchingString)
                if (match) break
            }
        }
        return match
    }
}