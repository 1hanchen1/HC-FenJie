package com.hanchen.hcfenjie.data.matching.imp

import com.hanchen.hcfenjie.data.matching.Matching
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class ContainsLore : Matching {
    override fun isMatching(itemStack: ItemStack, matchingString: String): Boolean {
        val typeName = matchingString.split("<->")[0]
        if (typeName == "containsLore") {
            val itemMeta = itemStack.itemMeta as ItemMeta
            if (itemMeta.hasLore()) {
                val loreList = itemMeta.lore
                for (lore in loreList!!) {
                    if (lore.contains(matchingString.split("<->")[1])) {
                        return true
                    }
                }
            }
        }
        return false
    }
}