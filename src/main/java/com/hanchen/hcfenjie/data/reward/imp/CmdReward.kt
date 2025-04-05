package com.hanchen.hcfenjie.data.reward.imp

import com.hanchen.hcfenjie.data.reward.Reward
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class CmdReward : Reward {
    override fun exeReward(player: Player, exeString: String) {
        val parts = exeString.split("<->")
        if (parts[0] == "cmd") {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), parts[1].replace("<player>", player.name))
        }
    }
}