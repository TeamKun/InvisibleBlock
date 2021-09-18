package net.kunmc.lab.invisibleblock.task;


import net.kunmc.lab.invisibleblock.block.InvisibleBlockData;
import net.kunmc.lab.invisibleblock.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.stream.Collectors;

public class Task extends BukkitRunnable {
    private JavaPlugin plugin;

    public Task(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        // Playerの更新処理を実行
        for (Player p : Bukkit.getOnlinePlayers().stream().collect(Collectors.toList())) {
            for (InvisibleBlockData ib: GameManager.targetBlock){

            }
        }
    }
}