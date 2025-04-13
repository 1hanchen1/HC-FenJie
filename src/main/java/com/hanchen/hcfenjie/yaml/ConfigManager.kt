package com.hanchen.hcfenjie.yaml

import com.hanchen.hcfenjie.util.LoggerUtil
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * 配置管理类（支持热重载）
 */
object ConfigManager {
    lateinit var plugin: JavaPlugin
    private var configFile: File? = null
    private var config: YamlConfiguration? = null

    var messagePrefix: String = ""
        private set

    // 动态配置缓存
    var debugMode: Boolean = false
        private set

    fun init(plugin: JavaPlugin) {
        this.plugin = plugin
        reload()
    }

    /**
     * 热重载配置
     */
    fun reload() {
        // 双重验证初始化状态
        require(::plugin.isInitialized) { "ConfigManager.plugin 必须在 reload() 前初始化" }

        plugin.reloadConfig()
        messagePrefix = plugin.config.getString("messages.prefix", "&e分解系统 &6>> ")
            ?.let { translateColorCodes(it) } ?: ""

        LoggerUtil.info("消息前缀加载: '$messagePrefix'")
        configFile = File(plugin.dataFolder, "Config.yml").also {
            if (!it.exists()) plugin.saveResource("Config.yml", false)
        }
        config = YamlConfiguration.loadConfiguration(configFile)
        plugin.reloadConfig()

        // 更新动态配置
        debugMode = config?.getBoolean("debug-mode", false) ?: false
        LoggerUtil.info("配置已重载 | 调试模式: ${if (debugMode) "开启" else "关闭"}")
    }

    private fun translateColorCodes(input: String): String {
        return input.replace('&', '§')
    }
}