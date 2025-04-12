package com.hanchen.hcfenjie.util

import com.hanchen.hcfenjie.Main
import org.bukkit.command.CommandSender

/**
 * 增强版消息工具类
 * 支持全系列颜色代码、安全检查、批量发送等功能
 */
object MessageUtil {
    // 预编译正则表达式提升性能
    private val COLOR_CODE_REGEX = Regex("&([0-9a-fk-orxA-FK-ORX])")

    /**
     * 安全发送消息（支持空值检查）
     * @param sender 接收者（可为null）
     * @param message 原始消息（支持null和空字符串）
     * @param logIfEmpty 当消息为空时是否记录日志
     */
    @JvmOverloads
    fun sendMessage(sender: CommandSender?, message: String?, logIfEmpty: Boolean = false) {
        when {
            sender == null -> {
                LoggerUtil.warn("尝试发送消息给null接收者")
                return
            }
            message.isNullOrEmpty() -> {
                if (logIfEmpty) LoggerUtil.debug("尝试发送空消息给 ${sender.name}")
                return
            }
            else -> {
                val formatted = translateAdvancedColorCodes(message)
                try {
                    sender.sendMessage(formatted)
                    // 调试模式提示
                    if (Main.instance.config.getBoolean("debug-mode", false)) {
                        LoggerUtil.debug("发送消息给 ${sender.name}: $formatted")
                    }
                } catch (e: Exception) {
                    LoggerUtil.error("消息发送失败: ${e.message} 接收者: ${sender.name}")
                }
            }
        }
    }

    /**
     * 增强颜色代码转换
     * 支持：
     * - 传统颜色代码 &0-&f
     * - 格式代码 &k-&r
     * - 十六进制颜色 &x&F&F&F&F&F&F
     * @return 转换后的消息（自动添加§前缀）
     */
    fun translateAdvancedColorCodes(input: String): String {
        return COLOR_CODE_REGEX.replace(input) {
            when (val code = it.groupValues[1].lowercase()) {
                "x" -> parseHexColor(it) // 处理十六进制颜色
                else -> "§$code"
            }
        }
    }

    /**
     * 批量发送统一消息
     * @param senders 接收者集合
     * @param message 消息内容
     */
    fun broadcast(senders: Collection<CommandSender>, message: String) {
        val formatted = translateAdvancedColorCodes(message)
        senders.forEach { sendMessage(it, formatted) }
        // 调试模式提示
        if (Main.instance.config.getBoolean("debug-mode", false)) {
            LoggerUtil.debug("广播消息: $formatted")
        }
    }

    /**
     * 解析十六进制颜色代码
     * @param match 匹配结果
     * @return 解析后的颜色代码
     */
    private fun parseHexColor(match: MatchResult): String {
        val hexStr = match.value.substringAfter('x').replace("&", "")
        return if (hexStr.length == 6) {
            "§x${hexStr.map { "§$it" }.joinToString("")}"
        } else {
            LoggerUtil.warn("无效的十六进制颜色代码: ${match.value}")
            ""
        }
    }
}