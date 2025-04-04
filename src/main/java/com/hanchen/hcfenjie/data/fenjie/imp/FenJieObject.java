package com.hanchen.hcfenjie.data.fenjie.imp;

import com.hanchen.hcfenjie.data.fenjie.FenJie;
import com.hanchen.hcfenjie.data.matching.Matching;
import com.hanchen.hcfenjie.data.matching.MatchingManage;
import com.hanchen.hcfenjie.data.reward.Reward;
import com.hanchen.hcfenjie.data.reward.RewardManage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class FenJieObject implements FenJie {
    private String fenJieName;
    private double fenJieChange;
    private List<String> fenJieRewardList;
    private List<String> fenJieMatchingList;

    public FenJieObject() {
    }

    public FenJieObject(String fenJieName, double fenJieChange, List<String> fenJieMatchingList, List<String> fenJieRewardList) {
        this.fenJieName = fenJieName;
        this.fenJieChange = fenJieChange;
        this.fenJieMatchingList = fenJieMatchingList;
        this.fenJieRewardList = fenJieRewardList;
    }

    @Override
    public String getFenJieName() {
        return this.fenJieName;
    }

    @Override
    public void setFenJieMatching(List<String> matchingList) {
        this.fenJieMatchingList = matchingList;
    }

    @Override
    public double getFenJieChange() {
        return this.fenJieChange;
    }

    @Override
    public void setFenJieChange(double change) {
        this.fenJieChange = change;
    }

    @Override
    public void setFenJieReward(List<String> rewardList) {
        this.fenJieRewardList = rewardList;
    }

    @Override
    public List<String> getFenJieReward() {
        return this.fenJieRewardList;
    }

    @Override
    public List<String> getFenJieMatching() {
        return this.fenJieMatchingList;
    }

    @Override
    public void exeReward(Player player) {
        for (String exeReward : this.fenJieRewardList) {
            String rewardType = exeReward.split("<->")[0];
            Reward reward = RewardManage.getReward(rewardType);
            if (reward != null && rewardType != null) {
                reward.exeReward(player, exeReward);
            }
        }
    }

    @Override
    public boolean isMatching(ItemStack itemStack) {
        boolean bj = false;
        for (String isMatching : this.fenJieMatchingList) {
            String matchingType = isMatching.split("<->")[0];
            Matching matching = MatchingManage.getMatching(matchingType);
            if (matching != null && matchingType != null) {
                bj = matching.isMatching(itemStack, isMatching);
            }
        }
        return bj;
    }
}
