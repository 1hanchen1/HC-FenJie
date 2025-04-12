package com.hanchen.hcfenjie.command

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.inventory.InventoryUtil
import com.hanchen.hcfenjie.util.MessageUtil
import com.hanchen.hcfenjie.util.LoggerUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

/**
 * 主命令处理器
 * 负责处理插件的命令逻辑
 */
class MainCommand : CommandExecutor, TabCompleter {
    /**
     * 处理命令执行
     * @param sender 命令发送者
     * @param command 命令对象
     * @param label 命令标签
     * @param args 命令参数
     * @return 命令执行结果
     */
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        // 检查命令是否属于插件
        if (command.name !in listOf("hcfj", "fj")) return true

        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("命令执行: $label ${args.joinToString(" ")}")
        }

        // 根据参数处理不同命令
        when {
            args.isEmpty() -> showHelp(sender) // 显示帮助信息
            else -> when (args[0].lowercase()) {
                "help" -> showHelp(sender) // 显示帮助信息
                "open" -> handleOpenCommand(sender) // 处理打开命令
                "reload" -> handleReloadCommand(sender) // 处理重载命令
                else -> MessageUtil.sendMessage(
                    sender,
                    (Main.instance.prefix + Main.instance.unknownCommandMessage?.replace("%command%", label))
                        ?: "§c未知命令。使用 /$label help 查看帮助。"
                )
            }
        }
        return true
    }

    /**
     * 显示帮助信息
     * @param sender 命令发送者
     */
    private fun showHelp(sender: CommandSender) {
        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("显示帮助信息")
        }

        // 获取帮助信息并发送
        val messages = listOfNotNull(
            Main.instance.helpHeaderMessage,
            Main.instance.helpCommandHelpMessage,
            Main.instance.helpCommandOpenMessage,
            Main.instance.helpCommandReloadMessage?.takeIf { sender.hasPermission("hcfj.reload") }
        )
        messages.forEach { MessageUtil.sendMessage(sender, it) }
    }

    /**
     * 处理打开命令
     * @param sender 命令发送者
     */
    private fun handleOpenCommand(sender: CommandSender) {
        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("处理打开命令")
        }

        // 检查发送者是否为玩家
        if (sender !is Player) {
            MessageUtil.sendMessage(sender,
                (Main.instance.prefix + Main.instance.notAPlayerMessage) ?: "§c只有玩家可以执行此命令！"
            )
            return
        }

        // 检查权限
        val permission = "hcfj.open"
        if (!sender.hasPermission(permission)) {
            MessageUtil.sendMessage(
                sender,
                (Main.instance.prefix + Main.instance.noPermissionMessage?.replace("%permission%", permission))
                    ?: "§c你没有权限 $permission 执行此命令！"
            )
            return
        }

        // 打开分解界面
        InventoryUtil.openInventory(sender)
        MessageUtil.sendMessage(sender,
            (Main.instance.prefix + Main.instance.openSuccessMessage) ?: "§a分解界面已打开！"
        )
    }

    /**
     * 处理重载命令
     * @param sender 命令发送者
     */
    private fun handleReloadCommand(sender: CommandSender) {
        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("处理重载命令")
        }

        // 检查权限
        val permission = "hcfj.reload"
        if (!sender.hasPermission(permission)) {
            MessageUtil.sendMessage(
                sender,
                (Main.instance.prefix + Main.instance.noPermissionMessage?.replace("%permission%", permission))
                    ?: "§c你没有权限 $permission 执行此命令！"
            )
            return
        }

        // 重载配置
        Main.instance.run {
            initDefaultYaml()
            initFenJie()
            MessageUtil.sendMessage(sender, (Main.instance.prefix + reloadSuccessMessage) ?: "§a配置重载成功！")
        }
    }

    /**
     * 处理命令补全
     * @param sender 命令发送者
     * @param command 命令对象
     * @param alias 命令别名
     * @param args 命令参数
     * @return 补全建议
     */
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<String>
    ): List<String> {
        if (command.name !in listOf("hcfj", "fj")) return emptyList()

        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("命令补全: $alias ${args.joinToString(" ")}")
        }

        // 根据参数大小提供补全建议
        return when (args.size) {
            1 -> getAvailableSubCommands(sender)
                .filter { it.startsWith(args[0].lowercase(), ignoreCase = true) }
            else -> emptyList()
        }
    }

    /**
     * 获取可用的子命令
     * @param sender 命令发送者
     * @return 可用的子命令列表
     */
    private fun getAvailableSubCommands(sender: CommandSender): List<String> {
        return mutableListOf("help", "open").apply {
            if (sender.hasPermission("hcfj.reload")) add("reload")
        }
    }
}