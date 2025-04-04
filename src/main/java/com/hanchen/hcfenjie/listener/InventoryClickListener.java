package com.hanchen.hcfenjie.listener;

import com.hanchen.hcfenjie.Main;
import com.hanchen.hcfenjie.data.fenjie.FenJie;
import com.hanchen.hcfenjie.data.fenjie.FenJieManage;
import com.hanchen.hcfenjie.util.ChangeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack;
        Inventory inventory = event.getClickedInventory();
        if (inventory != null && inventory.getTitle().equals(Main.getInstance().inventoryTitle)) {
            Player player = (Player) event.getWhoClicked();
            if (event.getSlot() == 49) {
                event.setCancelled(true);
                int yes = 0;
                int no = 0;
                for (int i = 0; i < inventory.getSize(); i++) {
                    if (i != 49 && (itemStack = inventory.getItem(i)) != null && itemStack.getType() != Material.AIR && itemStack.hasItemMeta()) {
                        for (FenJie fenJie : FenJieManage.getFenJieMap().values()) {
                            if (fenJie.isMatching(itemStack)) {
                                for (int a = 1; a <= itemStack.getAmount(); a++) {
                                    if (ChangeUtil.check(fenJie.getFenJieChange())) {
                                        yes++;
                                        fenJie.exeReward(player);
                                    } else {
                                        no++;
                                    }
                                }
                                inventory.setItem(i, new ItemStack(Material.AIR));
                            }
                        }
                    }
                }
                if (!Main.getInstance().message1.equals("none")) {
                    player.sendMessage(Main.getInstance().message1.replaceAll("<number>", String.valueOf(yes)));
                }
                if (!Main.getInstance().message2.equals("none")) {
                    player.sendMessage(Main.getInstance().message2.replaceAll("<number>", String.valueOf(no)));
                }
            }
        }
    }
}
