package com.hanchen.hcfenjie.yaml

import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class YamlObject(private val fileName: String, private val plugin: JavaPlugin) {
    private val file: File

    init {
        // 确保文件路径正确
        file = File(plugin.dataFolder, fileName)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs() // 如果父目录不存在，则创建
        }
    }

    fun saveDefaultConfig() {
        if (!file.exists()) {
            // 从插件资源中加载文件
            plugin.saveResource(fileName, false)
        }
    }

    fun reloadConfig(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(file)
    }

    fun getConfig(): YamlConfiguration {
        return YamlConfiguration.loadConfiguration(file)
    }
}