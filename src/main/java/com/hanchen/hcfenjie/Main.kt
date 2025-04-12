package com.hanchen.hcfenjie

import com.hanchen.hcfenjie.command.MainCommand
import com.hanchen.hcfenjie.data.fenjie.FenJieManage
import com.hanchen.hcfenjie.data.fenjie.imp.FenJieObject
import com.hanchen.hcfenjie.data.matching.MatchingManage
import com.hanchen.hcfenjie.data.matching.imp.ContainsLore
import com.hanchen.hcfenjie.data.matching.imp.ContainsName
import com.hanchen.hcfenjie.data.matching.imp.EqualsLore
import com.hanchen.hcfenjie.data.matching.imp.EqualsName
import com.hanchen.hcfenjie.data.reward.RewardManage
import com.hanchen.hcfenjie.data.reward.imp.CmdReward
import com.hanchen.hcfenjie.data.reward.imp.MythicItemReward
import com.hanchen.hcfenjie.data.reward.imp.NeigeItemReward
import com.hanchen.hcfenjie.listener.InventoryClickListener
import com.hanchen.hcfenjie.listener.InventoryCloseListener
import com.hanchen.hcfenjie.util.LoggerUtil
import com.hanchen.hcfenjie.util.MessageUtil
import com.hanchen.hcfenjie.yaml.YamlObject
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

/**
 * 插件主类
 * 负责插件的初始化、配置加载和事件注册
 */
class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
            private set
    }

    private var fenJieFileList: List<File> = ArrayList()
    private var fenJieManFile: File? = null
    private var configYaml: YamlObject? = null
    private var Example1Yaml: YamlObject? = null
    private var Example2Yaml: YamlObject? = null
    var inventoryTitle: String? = null
    var inventoryItemStack: ItemStack? = null

    var successMessage: String? = null
    var failedMessage: String? = null
    var noPermissionMessage: String? = null
    var notAPlayerMessage: String? = null
    var helpHeaderMessage: String? = null
    var helpCommandHelpMessage: String? = null
    var helpCommandOpenMessage: String? = null
    var helpCommandReloadMessage: String? = null
    var reloadSuccessMessage: String? = null
    var openSuccessMessage: String? = null
    var unknownCommandMessage: String? = null
    var prefix: String? = null
    var nodecompose: String? = null

    /**
     * 插件启用时的初始化逻辑
     */
    override fun onEnable() {
        instance = this
        initDefaultYaml()
        initFenJie()
        Bukkit.getPluginManager().registerEvents(InventoryClickListener(), this)
        Bukkit.getPluginManager().registerEvents(InventoryCloseListener(), this)
        getCommand("hcfj")!!.executor = MainCommand()

        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&a██╗  ██╗  ██████╗")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&a██║  ██║ ██╔════╝")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&a███████║ ██║")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&a██╔══██║ ██║")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&a██║  ██║  ██████╗")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&a╚═╝  ╚═╝  ╚═════╝")

        // 调试模式提示
        LoggerUtil.debug("插件已启用，调试模式已开启")

    }

    /**
     * 插件禁用时的清理逻辑
     */
    override fun onDisable() {
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&4██╗  ██╗  ██████╗")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&4██║  ██║ ██╔════╝")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&4███████║ ██║")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&4██╔══██║ ██║")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&4██║  ██║  ██████╗")
        MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&4╚═╝  ╚═╝  ╚═════╝")
        // 调试模式提示
        LoggerUtil.debug("插件已禁用，调试模式已关闭")
    }

    /**
     * 初始化分解逻辑
     */
    fun initFenJie() {
        // 加载消息配置
        prefix = configYaml!!.getConfig().getString("prefix")
        successMessage = configYaml!!.getConfig().getString("messages.success")
        failedMessage = configYaml!!.getConfig().getString("messages.failed")
        noPermissionMessage = configYaml!!.getConfig().getString("messages.no-permission")
        notAPlayerMessage = configYaml!!.getConfig().getString("messages.not-a-player")
        helpHeaderMessage = configYaml!!.getConfig().getString("messages.help-header")
        helpCommandHelpMessage = configYaml!!.getConfig().getString("messages.help-command-help")
        helpCommandOpenMessage = configYaml!!.getConfig().getString("messages.help-command-open")
        helpCommandReloadMessage = configYaml!!.getConfig().getString("messages.help-command-reload")
        reloadSuccessMessage = configYaml!!.getConfig().getString("messages.reload-success")
        openSuccessMessage = configYaml!!.getConfig().getString("messages.open-success")
        unknownCommandMessage = configYaml!!.getConfig().getString("messages.unknownCommandMessage")
        nodecompose = configYaml!!.getConfig().getString("messages.nodecompose")

        // 加载库存配置
        inventoryTitle = MessageUtil.translateAdvancedColorCodes(configYaml!!.getConfig().getString("inventory.title"))
        inventoryItemStack = ItemStack(configYaml!!.getConfig().getInt("inventory.itemStack.id"))
        val itemMeta = inventoryItemStack!!.itemMeta as ItemMeta
        itemMeta.displayName = MessageUtil.translateAdvancedColorCodes(configYaml!!.getConfig().getString("inventory.itemStack.name"))
        itemMeta.lore = configYaml!!.getConfig().getStringList("inventory.itemStack.lore").map { MessageUtil.translateAdvancedColorCodes(it) }
        inventoryItemStack!!.itemMeta = itemMeta

        // 加载分解文件
        fenJieManFile = File(".//plugins//HC-FenJie//FenJie")
        fenJieFileList = ArrayList()
        loadFenJieFileList(fenJieManFile!!)

        // 清空管理器
        FenJieManage.clear()
        MatchingManage.clear()
        RewardManage.clear()

        // 注册分解配置
        for (file in fenJieFileList) {
            val yaml = YamlConfiguration.loadConfiguration(file)
            val fenJieName = yaml.getString("name")
            val fenJieChange = yaml.getDouble("change")
            val rewardList = yaml.getStringList("reward")
            val matchingList = yaml.getStringList("matching")
            val fenJieObject = FenJieObject(fenJieName, fenJieChange, matchingList, rewardList)
            FenJieManage.register(fenJieName, fenJieObject)
        }

        // 注册奖励和匹配类型
        RewardManage.register("cmd", CmdReward())
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
            RewardManage.register("mm", MythicItemReward())
            MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&aMythicMobs物品支持已加载")
        } else {
            MessageUtil.sendMessage(Bukkit.getConsoleSender(),"$prefix&4未检测到MythicMobs，相关奖励功能已禁用")
        }
        if (Bukkit.getPluginManager().isPluginEnabled("NeigeItems")) {
            RewardManage.register("ni", NeigeItemReward())
            MessageUtil.sendMessage(Bukkit.getConsoleSender(), "$prefix&aNeigeItems物品支持已加载")
        } else {
            MessageUtil.sendMessage(Bukkit.getConsoleSender(),"$prefix&4未检测到NeigeItems，相关奖励功能已禁用")
        }
        MatchingManage.register("equalsName", EqualsName())
        MatchingManage.register("containsLore", ContainsLore())
        MatchingManage.register("containsName", ContainsName())
        MatchingManage.register("equalsLore", EqualsLore())

        // 调试模式提示
        LoggerUtil.debug("分解逻辑初始化完成")
    }

    /**
     * 初始化默认配置文件
     */
    fun initDefaultYaml() {
        configYaml = YamlObject("Config.yml", this)
        configYaml!!.saveDefaultConfig()
        configYaml!!.reloadConfig()
        Example1Yaml = YamlObject("FenJie/Example1.yml", this)
        Example1Yaml!!.saveDefaultConfig()
        Example1Yaml!!.reloadConfig()
        Example2Yaml = YamlObject("FenJie/Example2.yml", this)
        Example2Yaml!!.saveDefaultConfig()
        Example2Yaml!!.reloadConfig()

        // 调试模式提示
        LoggerUtil.debug("默认配置文件初始化完成")
    }

    /**
     * 加载分解文件列表
     * @param mainFile 主文件夹
     */
    private fun loadFenJieFileList(mainFile: File) {
        for (file in mainFile.listFiles()!!) {
            if (file.isDirectory) {
                loadFenJieFileList(file)
            } else {
                (fenJieFileList as ArrayList<File>).add(file)
            }
        }

        // 调试模式提示
        LoggerUtil.debug("加载分解文件列表完成，共加载 ${fenJieFileList.size} 个文件")
    }
}