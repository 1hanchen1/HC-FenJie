package com.hanchen.hcfenjie.command

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.inventory.InventoryUtil
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import java.util.*

class MainCommand : CommandExecutor, TabCompleter { // 实现 TabCompleter 接口
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (command.name == "hcfj" || command.name == "fj") {
            when {
                args.isEmpty() -> {
                    // 如果没有参数，直接显示帮助信息
                    showHelp(sender)
                    return true
                }
                args[0] == "help" -> {
                    // 如果输入了 help 子命令，显示帮助信息
                    showHelp(sender)
                    return true
                }
                args[0] == "open" -> {
                    if (sender is Player) {
                        val requiredPermission = "hcfj.open"
                        if (sender.hasPermission(requiredPermission)) {
                            // 打开分解界面
                            InventoryUtil.openInventory(sender)
                            MessageUtil.sendMessage(sender, Main.instance.openSuccessMessage!!)
                        } else {
                            // 玩家缺少权限
                            MessageUtil.sendMessage(sender, Main.instance.noPermissionMessage!!.replace("%permission%", requiredPermission))
                        }
                    } else {
                        // 如果不是玩家, 提示非玩家无法执行
                        MessageUtil.sendMessage(sender, Main.instance.notAPlayerMessage!!)
                    }
                    return true
                }
                args[0] == "reload" && sender.isOp -> {
                    // 重载配置文件
                    Main.instance.initDefaultYaml()
                    Main.instance.initFenJie()
                    MessageUtil.sendMessage(sender, Main.instance.reloadSuccessMessage!!)
                    return true
                }
            }
            return true
        }
        return true
    }

    // 显示帮助信息的方法
    private fun showHelp(sender: CommandSender) {
        MessageUtil.sendMessage(sender, Main.instance.helpHeaderMessage!!)
        MessageUtil.sendMessage(sender, Main.instance.helpCommandHelpMessage!!)
        MessageUtil.sendMessage(sender, Main.instance.helpCommandOpenMessage!!)
        if (sender.isOp) {
            MessageUtil.sendMessage(sender, Main.instance.helpCommandReloadMessage!!)
        }
    }

    // 实现 TabCompleter 的 onTabComplete 方法
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String>? {
        if (command.name != "hcfj" && command.name != "fj") return null

        val completions = mutableListOf<String>()

        if (args.isEmpty()) {
            // 如果没有输入参数，返回所有可能的子命令
            completions.addAll(listOf("help", "open"))
            if (sender.isOp) {
                completions.add("reload")
            }
        } else {
            val lastArg = args.last().lowercase(Locale.getDefault())
            if (lastArg.isEmpty()) {
                // 如果最后一个参数为空，仍然返回所有可能的子命令
                completions.addAll(listOf("help", "open"))
                if (sender.isOp) {
                    completions.add("reload")
                }
            } else {
                // 根据输入的前缀过滤可能的补全
                if (lastArg.startsWith("h")) {
                    completions.add("help")
                }
                if (lastArg.startsWith("o")) {
                    completions.add("open")
                }
                if (lastArg.startsWith("r") && sender.isOp) {
                    completions.add("reload")
                }
            }
        }

        return completions
    }
}