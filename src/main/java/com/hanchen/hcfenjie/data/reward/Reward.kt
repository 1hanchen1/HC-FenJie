package com.hanchen.hcfenjie.data.reward

import org.bukkit.entity.Player

interface Reward {
    fun exeReward(player: Player, str: String)
}