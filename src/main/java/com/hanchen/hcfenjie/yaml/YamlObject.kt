package com.hanchen.hcfenjie.yaml;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class YamlObject {
    private final String configName;
    private final File file;
    private FileConfiguration fileConfiguration;
    private final JavaPlugin javaPlugin;

    public YamlObject(String configName, JavaPlugin javaPlugin) {
        this.configName = configName;
        this.file = new File(javaPlugin.getDataFolder(), configName);
        this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        this.javaPlugin = javaPlugin;
    }

    public void saveDefaultConfig() {
        if (!this.file.exists()) {
            this.javaPlugin.saveResource(this.configName, false);
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
        }
    }

    public FileConfiguration getConfig() {
        return this.fileConfiguration;
    }

    public void saveConfig() {
        try {
            this.fileConfiguration.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reloadConfig() {
        try {
            this.fileConfiguration = YamlConfiguration.loadConfiguration(this.file);
            this.fileConfiguration.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return this.file;
    }
}
