package com.hanchen.hcfenjie.util

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.yaml.ConfigManager
import org.bukkit.command.CommandSender

/**
 * 增强版消息工具类
 * 支持全系列颜色代码、安全检查、批量发送等功能
 */
object MessageUtil {
    /**
     * 发送带前缀的消息（自动处理前缀占位符）
     */
    fun sendFormattedMessage(sender: CommandSender?, key: String, vararg placeholders: Pair<String, Any>) {
        val rawMessage = Main.instance.config?.getString("messages.$key") ?: return
        applyMessageFormatting(rawMessage, placeholders.toMap()).let {
            sendMessage(sender, it)
        }
    }
    /**
     * 应用消息格式化（前缀+占位符）
     */
    private fun applyMessageFormatting(raw: String, placeholders: Map<String, Any>): String {
        // 1. 处理前缀占位符
        var formatted = raw.replace("%prefix%", ConfigManager.messagePrefix)

        // 2. 如果没有显式使用%prefix%，自动添加前缀
        if (!raw.contains("%prefix%")) {
            formatted = ConfigManager.messagePrefix + formatted
        }

        // 使用安全类型转换
        // 分两步处理占位符
        placeholders.forEach { (key, value) ->
            formatted = formatted.replace("%$key%", value.toString())
        }

        // 处理多行消息中的颜色代码
        return formatted.lineSequence()
            .map { line -> translateAdvancedColorCodes(line) }
            .joinToString("\n") { line ->
                // 确保每行保留颜色代码继承
                if (line.startsWith("§")) line else "§r$line"
            }
    }
    // 修改正则表达式以正确匹配十六进制颜色代码
    private val COLOR_CODE_REGEX = Regex("&([0-9a-fk-orxA-FK-ORX]|x(&[0-9a-fA-F]){6})")

    /**
     * 安全发送消息（支持空值检查）
     * @param sender 接收者（可为null）
     * @param message 原始消息（支持null和空字符串）
     * @param logIfEmpty 当消息为空时是否记录日志
     */
    @JvmOverloads
    fun sendMessage(sender: CommandSender?, message: String?, logIfEmpty: Boolean = false) {
        // 添加空值过滤
        val safeMessage = message?.takeIf { it.isNotEmpty() } ?: run {
            if (logIfEmpty) LoggerUtil.debug("空消息已过滤")
            return
        }
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
                    LoggerUtil.debug("发送消息给 ${sender.name}: $formatted")
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
        var lastColorCode = ""
        return COLOR_CODE_REGEX.replace(input) { match ->
            val code = match.groupValues[1].lowercase()
            when (code) {
                "x" -> parseHexColor(match).also { lastColorCode = it }
                else -> {
                    val translated = "§$code"
                    lastColorCode = translated
                    translated
                }
            }
        }.replace("\n", "\n$lastColorCode") // 自动继承上一行颜色
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
        LoggerUtil.debug("广播消息: $formatted")
    }

    /**
     * 解析十六进制颜色代码
     * @param match 匹配结果
     * @return 解析后的颜色代码
     */
    private fun parseHexColor(match: MatchResult): String {
        // 获取完整的颜色代码部分（如 &x&F&F&F&F&F&F）
        val fullCode = match.value

        // 正确提取十六进制字符（过滤所有非十六进制字符）
        val hexStr = fullCode
            .substringAfter('x', "") // 从x开始截取
            .filter { it in '0'..'9' || it in 'a'..'f' || it in 'A'..'F' } // 安全过滤
            .take(6) // 最多取6位
            .padEnd(6, 'f') // 不足补f

        return if (hexStr.length == 6) {
            // 使用标准格式 §x§f§f§f§f§f§f
            "§x${hexStr.map { "§$it" }.joinToString("")}"
        } else {
            LoggerUtil.warn("无效的十六进制颜色代码: $fullCode")
            ""
        }
    }
}