package com.hanchen.hcfenjie.data.reward.imp;

import com.hanchen.hcfenjie.data.reward.Reward;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CmdReward implements Reward {
    @Override
    public void exeReward(Player player, String exeString) {
        if (exeString.split("<->")[0].equals("cmd")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), exeString.split("<->")[1].replaceAll("<player>", player.getName()));
        }
    }
}