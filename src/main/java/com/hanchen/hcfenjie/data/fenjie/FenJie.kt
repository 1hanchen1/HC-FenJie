package com.hanchen.hcfenjie.data.fenjie;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface FenJie {
    String getFenJieName();

    double getFenJieChange();

    void setFenJieChange(double d);

    List<String> getFenJieReward();

    void setFenJieReward(List<String> list);

    List<String> getFenJieMatching();

    void setFenJieMatching(List<String> list);

    void exeReward(Player player);

    boolean isMatching(ItemStack itemStack);
}