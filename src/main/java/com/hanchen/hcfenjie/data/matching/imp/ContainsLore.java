package com.hanchen.hcfenjie.data.matching.imp;

import com.hanchen.hcfenjie.data.matching.Matching;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.List;

public class ContainsLore implements Matching {
    @Override
    public boolean isMatching(ItemStack itemStack, String matchingString) {
        String typeName = matchingString.split("<->")[0];
        if (typeName.equals("containsLore")) {
            boolean bj = false;
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta.hasLore()) {
                List<String> loreS = itemMeta.getLore();
                Iterator<String> it = loreS.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    String lore = it.next();
                    if (lore.contains(matchingString.split("<->")[1])) {
                        bj = true;
                        break;
                    }
                }
            }
            return bj;
        }
        return false;
    }
}