package com.hanchen.hcfenjie;

import com.hanchen.hcfenjie.command.MainCommand;
import com.hanchen.hcfenjie.data.fenjie.FenJieManage;
import com.hanchen.hcfenjie.data.fenjie.imp.FenJieObject;
import com.hanchen.hcfenjie.data.matching.MatchingManage;
import com.hanchen.hcfenjie.data.matching.imp.ContainsLore;
import com.hanchen.hcfenjie.data.matching.imp.EqualsName;
import com.hanchen.hcfenjie.data.reward.RewardManage;
import com.hanchen.hcfenjie.data.reward.imp.CmdReward;
import com.hanchen.hcfenjie.listener.InventoryClickListener;
import com.hanchen.hcfenjie.listener.InventoryCloseListener;
import com.hanchen.hcfenjie.yaml.YamlObject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Main extends JavaPlugin {
    private static Main main;
    public List<File> fenJieFileList;
    public File fenJieManFile;
    public YamlObject ConfigYaml;
    public YamlObject equalsNameYaml;
    public YamlObject equalsLoreYaml;
    public YamlObject equalsItemYaml;
    public YamlObject ContainsNameYaml;
    public YamlObject ContainsLoreYaml;
    public String inventoryTitle;
    public ItemStack inventoryItemStack;
    public String message1;
    public String message2;

    public void onEnable() {
        main = this;
        initDefaultYaml();
        initFenJie();
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), main);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), main);
        Bukkit.getPluginCommand("hcfj").setExecutor(new MainCommand());
        getLogger().info("[HC-FenJie]启动成功");
    }

    public void onDisable() {
        getLogger().info("[HC-FenJie]卸载成功");
    }

    public static Main getInstance() {
        return main;
    }

    public void initFenJie() {
        this.message1 = this.ConfigYaml.getConfig().getString("message.1");
        this.message2 = this.ConfigYaml.getConfig().getString("message.2");
        this.inventoryTitle = this.ConfigYaml.getConfig().getString("inventory.title");
        this.inventoryItemStack = new ItemStack(this.ConfigYaml.getConfig().getInt("inventory.itemStack.id"));
        ItemMeta itemMeta = this.inventoryItemStack.getItemMeta();
        itemMeta.setDisplayName(this.ConfigYaml.getConfig().getString("inventory.itemStack.name"));
        itemMeta.setLore(this.ConfigYaml.getConfig().getStringList("inventory.itemStack.lore"));
        this.inventoryItemStack.setItemMeta(itemMeta);
        this.fenJieManFile = new File(".//plugins//HC-FenJie//FenJie");
        this.fenJieFileList = new ArrayList<>();
        loadFenJieFileList(this.fenJieManFile);
        FenJieManage.getFenJieMap().clear();
        MatchingManage.getMatchingMap().clear();
        RewardManage.getRewardMap().clear();
        for (File fenJie : this.fenJieFileList) {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(fenJie);
            String fenJieName = yaml.getString("name");
            double fenJieChange = yaml.getDouble("change");
            List<String> rewardList = yaml.getStringList("reward");
            List<String> matchingList = yaml.getStringList("matching");
            FenJieObject fenJieObject = new FenJieObject(fenJieName, fenJieChange, matchingList, rewardList);
            FenJieManage.register(fenJieName, fenJieObject);
        }
        RewardManage.register("cmd", new CmdReward());
        MatchingManage.register("equalsName", new EqualsName());
        MatchingManage.register("containsLore", new ContainsLore());
    }

    public void initDefaultYaml() {
        this.ConfigYaml = new YamlObject("Config.yml", main);
        this.ConfigYaml.saveDefaultConfig();
        this.ConfigYaml.reloadConfig();
        this.equalsNameYaml = new YamlObject("FenJie/EqualsName.yml", main);
        this.equalsNameYaml.saveDefaultConfig();
        this.equalsNameYaml.reloadConfig();
        this.ContainsLoreYaml = new YamlObject("FenJie/ContainsLore.yml", main);
        this.ContainsLoreYaml.saveDefaultConfig();
        this.ContainsLoreYaml.reloadConfig();
    }

    public void loadFenJieFileList(File mainFile) {
        for (File file : (File[]) Objects.requireNonNull(mainFile.listFiles())) {
            if (file.isDirectory()) {
                loadFenJieFileList(file);
            } else {
                this.fenJieFileList.add(file);
            }
        }
    }
}