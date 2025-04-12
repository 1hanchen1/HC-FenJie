package com.hanchen.hcfenjie.util

import org.bukkit.command.CommandSender

object MessageUtil {
    /**
     * 自动处理颜色代码并发送消息。
     * @param sender 消息接收者
     * @param message 原始消息
     */
    fun sendMessage(sender: CommandSender, message: String) {
        val formattedMessage = translateColorCodes(message)
        sender.sendMessage(formattedMessage)
    }

    /**
     * 将 & 替换为 §，以便 Minecraft 能够解析颜色代码。
     * @param input 输入的字符串
     * @return 替换后的字符串
     */
    fun translateColorCodes(input: String): String {
        return input.replace("&".toRegex(), "§")
    }
}