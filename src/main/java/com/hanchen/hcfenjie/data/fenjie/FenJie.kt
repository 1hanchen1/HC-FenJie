package com.hanchen.hcfenjie.data.fenjie

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface FenJie {
    fun getFenJieName(): String
    fun getFenJieChange(): Double
    fun setFenJieChange(change: Double)
    fun getFenJieReward(): List<String>
    fun setFenJieReward(rewardList: List<String>)
    fun getFenJieMatching(): List<String>
    fun setFenJieMatching(matchingList: List<String>)
    fun exeReward(player: Player)
    fun isMatching(itemStack: ItemStack): Boolean
}