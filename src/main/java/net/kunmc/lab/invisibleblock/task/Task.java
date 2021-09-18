package net.kunmc.lab.invisibleblock.task;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedBlockData;
import net.kunmc.lab.invisibleblock.Config;
import net.kunmc.lab.invisibleblock.block.BlockConvert;
import net.kunmc.lab.invisibleblock.block.InvisibleBlockData;
import net.kunmc.lab.invisibleblock.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Task extends BukkitRunnable {
    private JavaPlugin plugin;

    public Task(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        //if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
        //    return;

        // Playerの更新処理を実行
        List<InvisibleBlockData> shouldRemoveBlocks = new ArrayList<>();
        for (InvisibleBlockData ib: GameManager.sendTargetBlock){
            ib.incrementTick();
            // Tick
            if (ib.tick < Config.time * 20) continue;

            BlockConvert.changeToBarrier(ib.block);
            shouldRemoveBlocks.add(ib);
        }
        for (InvisibleBlockData ib: shouldRemoveBlocks){
            GameManager.sendTargetBlock.remove(ib);
            GameManager.targetBlock.add(ib);
        }
    }
}