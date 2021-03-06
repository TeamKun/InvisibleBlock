package net.kunmc.lab.invisibleblock;

import net.kunmc.lab.invisibleblock.block.BlockConvert;
import net.kunmc.lab.invisibleblock.block.BlockEvent;
import net.kunmc.lab.invisibleblock.command.CommandConst;
import net.kunmc.lab.invisibleblock.command.CommandController;
import net.kunmc.lab.invisibleblock.game.GameManager;
import net.kunmc.lab.invisibleblock.task.Task;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class InvisibleBlock extends JavaPlugin {
    private static InvisibleBlock plugin;
    private BukkitTask task;

    public static InvisibleBlock getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Config.loadConfig();
        getCommand(CommandConst.MAIN_COMMAND).setExecutor(new CommandController());
        getServer().getPluginManager().registerEvents(new BlockEvent(), plugin);

        task = new Task(plugin).runTaskTimer(this, 0, 1);
        getLogger().info("InvisibleBlock Plugin is enabled");
    }

    @Override
    public void onDisable() {
        GameManager.sendTargetBlock.clear();
        while (GameManager.targetBlock.size() > 0) {
            BlockConvert.revertBlock();
        }
        getLogger().info("InvisibleBlock Plugin is disabled");
    }
}
