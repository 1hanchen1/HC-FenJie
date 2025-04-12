package com.hanchen.hcfenjie.util

import com.hanchen.hcfenjie.Main
import com.hanchen.hcfenjie.util.MessageUtil
import org.bukkit.ChatColor

/**
 * 插件日志工具类
 * 提供分级日志输出功能，支持颜色代码
 */
object LoggerUtil {
    // 获取插件主类实例
    private val plugin = Main.instance

    /**
     * 信息级别日志 (绿色)
     * @param message 日志内容（支持颜色代码）
     */
    fun info(message: String) {
        val coloredMessage = MessageUtil.translateAdvancedColorCodes(message)
        plugin.logger?.info(coloredMessage)
        // 调试模式提示
        if (plugin.config.getBoolean("debug-mode", false)) {
            debug("INFO: $message")
        }
    }

    /**
     * 警告级别日志 (黄色)
     * @param message 日志内容（支持颜色代码）
     */
    fun warn(message: String) {
        val coloredMessage = MessageUtil.translateAdvancedColorCodes(message)
        plugin.logger?.warning(coloredMessage)
        // 调试模式提示
        if (plugin.config.getBoolean("debug-mode", false)) {
            debug("WARN: $message")
        }
    }

    /**
     * 错误级别日志 (红色)
     * @param message 日志内容（支持颜色代码）
     */
    fun error(message: String) {
        val coloredMessage = MessageUtil.translateAdvancedColorCodes(message)
        plugin.logger?.severe(coloredMessage)
        // 调试模式提示
        if (plugin.config.getBoolean("debug-mode", false)) {
            debug("ERROR: $message")
        }
    }

    /**
     * 调试日志 (蓝色，仅在调试模式启用)
     * @param message 日志内容（支持颜色代码）
     */
    fun debug(message: String) {
        if (plugin.config.getBoolean("debug-mode", false)) {
            val coloredMessage = MessageUtil.translateAdvancedColorCodes(message)
            plugin.logger?.info(coloredMessage)
        }
    }
}