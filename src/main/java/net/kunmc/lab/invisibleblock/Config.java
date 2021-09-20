package net.kunmc.lab.invisibleblock;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    // ブロックが消えるまでの時間
    public static int time;
    public static int maxRevertNum;

    public static void loadConfig() {

        InvisibleBlock plugin = InvisibleBlock.getPlugin();

        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        time = config.getInt("time");
        maxRevertNum = config.getInt("maxRevertNum");
    }
}