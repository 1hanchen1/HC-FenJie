package com.hanchen.hcfenjie.data.matching.imp;

import com.hanchen.hcfenjie.data.matching.Matching;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EqualsName implements Matching {
    @Override // me.erpang.epfenjie.data.matching.Matching
    public boolean isMatching(ItemStack itemStack, String matchingString) {
        String typeName = matchingString.split("<->")[0];
        if (typeName.equals("equalsName")) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.hasDisplayName()) {
                String itemName = itemMeta.getDisplayName();
                return itemName.equals(matchingString.split("<->")[1]);
            }
            return false;
        }
        return false;
    }
}