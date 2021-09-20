package net.kunmc.lab.invisibleblock.block;

import net.kunmc.lab.invisibleblock.Config;
import net.kunmc.lab.invisibleblock.game.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockConvert {
    public static void changeToBarrier(Block block){
        block.setType(Material.BARRIER);
    }

    public static void revertBlock(){
        // 1Tickあたり2000ブロックまでの処理に留めておく
        List<String> reverted = new ArrayList<>();
        int cnt = 0;
        for (InvisibleBlockData ib : GameManager.targetBlock.values()) {
            ib.block.setBlockData(ib.blockData);
            reverted.add(BlockConvert.createBlockLocationKey(ib.block));
            cnt ++;
            if (cnt >= Config.maxRevertNum)break;
        }
        for (String key: reverted){
            GameManager.targetBlock.remove(key);
        }
    }

    public static String createBlockLocationKey(Block block){
        Location loc = block.getLocation();
        return loc.getX() +" "+ loc.getY() +" "+loc.getZ();
    }
}