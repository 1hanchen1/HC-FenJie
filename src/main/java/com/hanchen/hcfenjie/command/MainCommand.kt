package com.hanchen.hcfenjie.command

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.inventory.InventoryUtil
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import com.hanchen.hcfenjie.yaml.ConfigManager
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
        LoggerUtil.debug("命令执行: $label ${args.joinToString(" ")}")

        // 根据参数处理不同命令
        when {
            args.isEmpty() -> showHelp(sender) // 显示帮助信息
            else -> when (args[0].lowercase()) {
                "help" -> showHelp(sender) // 显示帮助信息
                "open" -> handleOpenCommand(sender) // 处理打开命令
                "reload" -> handleReloadCommand(sender) // 处理重载命令
                else -> MessageUtil.sendFormattedMessage(
                    sender,
                    "unknown-command",
                    "command" to label
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
        LoggerUtil.debug("显示帮助信息")

        // 使用消息键列表动态生成帮助信息
        val messageKeys = listOfNotNull(
            "help-header",
            "help-help",
            "help-open",
            "help-reload".takeIf { sender.hasPermission("hcfj.reload") },
            "help-footer",
            "help-version"  // 新增版本信息
        )

        messageKeys.forEach { key ->
            MessageUtil.sendFormattedMessage(
                sender,
                key,
                "version" to Main.instance.description.version
            )
        }
    }


    /**
     * 处理打开命令
     * @param sender 命令发送者
     */
    private fun handleOpenCommand(sender: CommandSender) {
        // 调试模式提示
            LoggerUtil.debug("处理打开命令")

        // 检查发送者是否为玩家
        if (sender !is Player) {
            MessageUtil.sendFormattedMessage(sender, "not-player")
            return
        }

        MessageUtil.sendFormattedMessage(
            sender,
            if (sender.hasPermission("hcfj.open")) "open-success" else "no-permission",
            "permission" to "hcfj.open"
        ).run {
            InventoryUtil.openInventory(sender)
        }
    }

    /**
     * 处理重载命令
     * @param sender 命令发送者
     */
    private fun handleReloadCommand(sender: CommandSender) {
        // 调试模式提示
        LoggerUtil.debug("处理重载命令")

        if (!sender.hasPermission("hcfj.reload")) {
            MessageUtil.sendFormattedMessage(
                sender,
                "no-permission",
                "permission" to "hcfj.reload"
            )
            return
        }

        // 重载配置
        Main.instance.run {
            initDefaultYaml()
            initFenJie()
            ConfigManager.reload()
            MessageUtil.sendFormattedMessage(sender, "reload-success")
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
        LoggerUtil.debug("命令补全: $alias ${args.joinToString(" ")}")

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