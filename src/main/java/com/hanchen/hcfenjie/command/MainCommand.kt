package com.hanchen.hcfenjie.command;

import com.hanchen.hcfenjie.Main;
import com.hanchen.hcfenjie.inventory.InventoryUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equals("hcfj")) {
            if (args.length == 0) {
                sender.sendMessage("/hcfj open 打开分解界面");
                sender.sendMessage("/hcfj reload 重载配置文件");
                return true;
            }
            if (args[0].equals("open") && (sender instanceof Player) && sender.hasPermission("hcfj.open")) {
                Player player = (Player) sender;
                InventoryUtil.openInventory(player);
                return true;
            }
            if (args[0].equals("reload") && sender.isOp()) {
                Main.getInstance().initDefaultYaml();
                Main.getInstance().initFenJie();
                sender.sendMessage("重载成功");
                return true;
            }
            return true;
        }
        return true;
    }
}