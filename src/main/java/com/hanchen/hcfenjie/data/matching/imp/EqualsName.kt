package com.hanchen.hcfenjie.data.matching.imp

import com.hanchen.hcfenjie.data.matching.Matching
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class EqualsName : Matching {
    override fun isMatching(itemStack: ItemStack, matchingString: String): Boolean {
        val typeName = matchingString.split("<->")[0]
        if (typeName == "equalsName") {
            val itemMeta = itemStack.itemMeta as ItemMeta
            if (itemMeta.hasDisplayName()) {
                val itemName = itemMeta.displayName
                return itemName == matchingString.split("<->")[1]
            }
        }
        return false
    }
}