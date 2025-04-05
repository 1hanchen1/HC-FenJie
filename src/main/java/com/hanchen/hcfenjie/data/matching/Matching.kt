package com.hanchen.hcfenjie.data.matching

import org.bukkit.inventory.ItemStack

interface Matching {
    fun isMatching(itemStack: ItemStack, str: String): Boolean
}