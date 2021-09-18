package net.kunmc.lab.invisibleblock;

import net.kunmc.lab.invisibleblock.command.CommandConst;
import org.bukkit.configuration.file.FileConfiguration;

public class Config {

    // ブロックが消えるまでの時間
    public static int time;

    public static void loadConfig() {

        InvisibleBlock plugin = InvisibleBlock.getPlugin();

        plugin.saveDefaultConfig();
        FileConfiguration config = plugin.getConfig();

        time = config.getInt("time");
    }
}