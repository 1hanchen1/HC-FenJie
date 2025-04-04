package com.hanchen.hcfenjie.listener;

import com.hanchen.hcfenjie.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InventoryCloseListener implements Listener {
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        ItemStack itemStack;
        Inventory inventory = event.getInventory();
        if (inventory != null && inventory.getTitle().equals(Main.getInstance().inventoryTitle)) {
            Player player = (Player) event.getPlayer();
            List<ItemStack> itemStackList = new ArrayList<>();
            for (int i = 0; i < inventory.getSize(); i++) {
                if (i != 49 && (itemStack = inventory.getItem(i)) != null && itemStack.getType() != Material.AIR) {
                    itemStackList.add(itemStack);
                }
            }
            Iterator<ItemStack> it = itemStackList.iterator();
            while (it.hasNext()) {
                player.getInventory().addItem(new ItemStack[]{it.next()});
            }
        }
    }

}
