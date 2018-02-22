package com.github.manolo8.simplemachines;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @author Willian
 */
public final class Config {

    public boolean REMOVE_MACHINE_WRONG;
    private final FileConfiguration config;
    private final double _VERSION = 1.2;

    public Config(SimpleMachines plugin) {
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
        if (this.config.getDouble("VERSION") != _VERSION) {
            File file = new File(plugin.getDataFolder(), "config.yml");
            file.renameTo(new File(plugin.getDataFolder(), "oldConfig.yml"));
            file.delete();
            File book = new File(plugin.getDataFolder(), "book.txt");
            book.renameTo(new File(plugin.getDataFolder(), "oldBook.txt"));
            book.delete();
            plugin.saveDefaultConfig();
            Bukkit.getLogger().info("---------------------------------");
            Bukkit.getLogger().info("A NEW CONFIG FILE HAS GENERATED!");
            Bukkit.getLogger().info("---------------------------------");
        }
        this.REMOVE_MACHINE_WRONG = getConfig().getBoolean("REMOVE_MACHINE_WRONG", false);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public String getString(String path) {
        return getString(path, null);
    }

    public String getString(String path, String def) {
        return config.getString(path, def);
    }
}
