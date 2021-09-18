package net.kunmc.lab.invisibleblock.block;

import net.kunmc.lab.invisibleblock.game.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockEvent implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        GameManager.targetBlock.add(new InvisibleBlockData(e.getBlock()));
    }
}