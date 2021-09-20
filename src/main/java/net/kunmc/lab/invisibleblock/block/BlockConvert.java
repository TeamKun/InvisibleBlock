package net.kunmc.lab.invisibleblock.block;

import net.kunmc.lab.invisibleblock.Config;
import net.kunmc.lab.invisibleblock.InvisibleBlock;
import net.kunmc.lab.invisibleblock.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

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
            String key = BlockConvert.createBlockLocationKey(ib.block);
            ib.block.setType(ib.blockData.getMaterial());
            ib.block.setBlockData(ib.blockData);
            if (ib.blockData instanceof org.bukkit.block.data.type.Sign && GameManager.signBlock.containsKey(key)) {
                List<String> lines = GameManager.signBlock.get(key);
                Sign sign = (Sign)ib.block.getState();
                for (int i = 0; i < lines.size(); i++) {
                    sign.setLine(i, lines.get(i));
                }
                sign.update();
            }
            reverted.add(key);
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