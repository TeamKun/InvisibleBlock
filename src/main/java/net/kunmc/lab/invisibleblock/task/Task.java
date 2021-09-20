package net.kunmc.lab.invisibleblock.task;

import net.kunmc.lab.invisibleblock.Config;
import net.kunmc.lab.invisibleblock.block.BlockConvert;
import net.kunmc.lab.invisibleblock.block.InvisibleBlockData;
import net.kunmc.lab.invisibleblock.game.GameManager;
import net.kunmc.lab.invisibleblock.util.DecolationConst;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

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
        for (InvisibleBlockData ib: GameManager.sendTargetBlock.values()) {
            ib.incrementTick();
            // Tick
            if (ib.tick < Config.time * 20) continue;

            BlockConvert.changeToBarrier(ib.block);
            shouldRemoveBlocks.add(ib);
        }
        for (InvisibleBlockData ib: shouldRemoveBlocks) {
            // 処理タイミングなどで存在しなくても問題ないようにチェックしから削除
            String key = BlockConvert.createBlockLocationKey(ib.block);
            if (GameManager.sendTargetBlock.containsKey(key)) {
                GameManager.sendTargetBlock.remove(key);
                GameManager.targetBlock.put(key, ib);
            }
        }

        if (GameManager.isRevertBlock) {
            BlockConvert.revertBlock();
            // revert対象がいなくなったらゲーム開始前に戻す
            if (GameManager.targetBlock.size() == 0) {
                GameManager.controller(GameManager.GameMode.MODE_NEUTRAL);
                Bukkit.broadcastMessage(DecolationConst.GREEN + "可視化完了");
            }
        }
    }
}