package com.hanchen.hcfenjie

import com.hanchen.hcfenjie.command.MainCommand
import com.hanchen.hcfenjie.data.fenjie.FenJieManage
import com.hanchen.hcfenjie.data.fenjie.imp.FenJieObject
import com.hanchen.hcfenjie.data.matching.MatchingManage
import com.hanchen.hcfenjie.data.matching.imp.ContainsLore
import com.hanchen.hcfenjie.data.matching.imp.EqualsName
import com.hanchen.hcfenjie.data.reward.RewardManage
import com.hanchen.hcfenjie.data.reward.imp.CmdReward
import com.hanchen.hcfenjie.listener.InventoryClickListener
import com.hanchen.hcfenjie.listener.InventoryCloseListener
import com.hanchen.hcfenjie.yaml.YamlObject
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
            private set
    }

    var fenJieFileList: List<File> = ArrayList()
    var fenJieManFile: File? = null
    var configYaml: YamlObject? = null
    var equalsNameYaml: YamlObject? = null
    var equalsLoreYaml: YamlObject? = null
    var equalsItemYaml: YamlObject? = null
    var containsNameYaml: YamlObject? = null
    var containsLoreYaml: YamlObject? = null
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

    override fun onEnable() {
        instance = this
        initDefaultYaml()
        initFenJie()
        Bukkit.getPluginManager().registerEvents(InventoryClickListener(), this)
        Bukkit.getPluginManager().registerEvents(InventoryCloseListener(), this)
        getCommand("hcfj")!!.setExecutor(MainCommand())
        logger.info("[HC-FenJie] 启动成功")
    }

    override fun onDisable() {
        logger.info("[HC-FenJie] 卸载成功")
    }

    fun initFenJie() {
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

        inventoryTitle = configYaml!!.getConfig().getString("inventory.title")
        inventoryItemStack = ItemStack(configYaml!!.getConfig().getInt("inventory.itemStack.id"))
        val itemMeta = inventoryItemStack!!.itemMeta as ItemMeta
        itemMeta.displayName = configYaml!!.getConfig().getString("inventory.itemStack.name")
        itemMeta.lore = configYaml!!.getConfig().getStringList("inventory.itemStack.lore")
        inventoryItemStack!!.itemMeta = itemMeta
        fenJieManFile = File(".//plugins//HC-FenJie//FenJie")
        fenJieFileList = ArrayList()
        loadFenJieFileList(fenJieManFile!!)

        FenJieManage.clear()
        MatchingManage.getMatchingMap().clear()
        RewardManage.getRewardMap().clear()

        for (file in fenJieFileList) {
            val yaml = YamlConfiguration.loadConfiguration(file)
            val fenJieName = yaml.getString("name")
            val fenJieChange = yaml.getDouble("change")
            val rewardList = yaml.getStringList("reward")
            val matchingList = yaml.getStringList("matching")
            val fenJieObject = FenJieObject(fenJieName, fenJieChange, matchingList, rewardList)
            FenJieManage.register(fenJieName, fenJieObject)
        }
        RewardManage.register("cmd", CmdReward())
        MatchingManage.register("equalsName", EqualsName())
        MatchingManage.register("containsLore", ContainsLore())
    }

    fun initDefaultYaml() {
        configYaml = YamlObject("Config.yml", this)
        configYaml!!.saveDefaultConfig()
        configYaml!!.reloadConfig()
        equalsNameYaml = YamlObject("FenJie/EqualsName.yml", this)
        equalsNameYaml!!.saveDefaultConfig()
        equalsNameYaml!!.reloadConfig()
        containsLoreYaml = YamlObject("FenJie/ContainsLore.yml", this)
        containsLoreYaml!!.saveDefaultConfig()
        containsLoreYaml!!.reloadConfig()
    }

    fun loadFenJieFileList(mainFile: File) {
        for (file in mainFile.listFiles()!!) {
            if (file.isDirectory) {
                loadFenJieFileList(file)
            } else {
                (fenJieFileList as ArrayList<File>).add(file)
            }
        }
    }
}