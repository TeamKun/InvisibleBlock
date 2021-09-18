package net.kunmc.lab.invisibleblock.block;

import net.kunmc.lab.invisibleblock.game.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.awt.*;
import java.util.Set;

public class BlockEvent implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        //if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
        //    return;

        Block b = e.getBlock();
        BlockData bd = e.getBlock().getBlockData();
        // 2Block以上はonBlockMultiPlaceで処理
        if (bd instanceof Bed || bd instanceof Door) return;

        InvisibleBlockData ib = new InvisibleBlockData(b, bd.clone());
        GameManager.sendTargetBlock.add(ib);
    }

    @EventHandler
    public void onBlockMultiPlace(BlockMultiPlaceEvent e) {
        //if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
        //    return;

        Block b = e.getBlock();
        BlockData bd = e.getBlock().getBlockData();
        Location loc = e.getBlock().getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        World w = e.getPlayer().getWorld();
        GameManager.sendTargetBlock.add(new InvisibleBlockData(b, bd.clone()));

        if (bd instanceof Bed) {
            int[][] point = { {x+1,z}, {x,z+1}, {x-1,z}, {x, z-1}};
            for (int[] p: point) {
                Block checkBlock = w.getBlockAt(p[0], y, p[1]);
                if(isTargetBed(b, checkBlock)){
                    GameManager.sendTargetBlock.add(new InvisibleBlockData(checkBlock, checkBlock.getBlockData().clone()));
                }
            }
        }
    }

    private boolean isTargetBed(Block b, Block checkBlock) {
        if (!(checkBlock.getBlockData() instanceof Bed)) return false;

        Bed.Part checkPart = Bed.Part.HEAD;
        if (((Bed)b.getBlockData()).getPart() == Bed.Part.HEAD){
            checkPart = Bed.Part.FOOT;
        }

        if (((Bed)checkBlock.getBlockData()).getPart() == checkPart && ((Bed)b.getBlockData()).getFacing() == ((Bed) checkBlock.getBlockData()).getFacing()) {
            return true;
        }

        return false;
    }
}