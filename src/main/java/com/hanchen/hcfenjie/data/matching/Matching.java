package com.hanchen.hcfenjie.data.matching;

import org.bukkit.inventory.ItemStack;

public interface Matching {
    boolean isMatching(ItemStack itemStack, String str);
}