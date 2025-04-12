package com.hanchen.hcfenjie.yaml

import com.hanchen.hcfenjie.util.LoggerUtil
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.IOException

/**
 * 增强版 YAML 配置文件管理类
 * 支持缓存、自动保存、异常处理等功能
 */
class YamlObject (
    private val fileName: String,
    private val plugin: JavaPlugin
) {
    private val file: File
    private var cachedConfig: YamlConfiguration? = null

    init {
        file = File(plugin.dataFolder, fileName).apply {
            parentFile.takeUnless { it.exists() }?.let { dir ->
                if (!dir.mkdirs()) {
                    LoggerUtil.error("无法创建配置目录: ${dir.absolutePath}")
                }
            }
        }
    }

    /**
     * 初始化配置文件
     * @param overwrite 是否覆盖已存在的文件
     */
    fun saveDefaultConfig(overwrite: Boolean = false) {
        try {
            if (!file.exists() || overwrite) {
                plugin.getResource(fileName)?.let {
                    plugin.saveResource(fileName, overwrite)
                    LoggerUtil.debug("已保存默认配置文件: $fileName")
                } ?: LoggerUtil.warn("插件资源中未找到配置文件: $fileName")
            }
        } catch (e: Exception) {
            LoggerUtil.error("保存配置文件失败: ${e.message}")
        }
    }

    /**
     * 重载并缓存配置
     * @return 最新的配置实例
     */
    fun reloadConfig(): YamlConfiguration {
        return try {
            cachedConfig = YamlConfiguration.loadConfiguration(file).also {
                LoggerUtil.debug("已重新加载配置文件: $fileName")
            }
            cachedConfig!!
        } catch (e: IOException) {
            LoggerUtil.error("加载配置文件失败: ${e.message}")
            YamlConfiguration()
        }
    }

    /**
     * 获取缓存的配置实例
     */
    fun getConfig(): YamlConfiguration {
        return cachedConfig ?: reloadConfig()
    }

    /**
     * 将内存配置写入文件
     */
    fun saveConfig() {
        try {
            getConfig().save(file)
            LoggerUtil.debug("已保存配置文件: $fileName")
        } catch (e: IOException) {
            LoggerUtil.error("保存配置到文件失败: ${e.message}")
        }
    }

    /**
     * 更新配置内容并自动保存
     * @param block 配置修改逻辑
     */
    fun updateConfig(block: YamlConfiguration.() -> Unit) {
        getConfig().apply(block)
        saveConfig()
    }
}