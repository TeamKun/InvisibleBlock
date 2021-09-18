package net.kunmc.lab.invisibleblock.block;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class BlockConvert {
    public static void changeToBarrier(Block block){
        block.setType(Material.BARRIER);
    }

    public static void revertBlock(Block beforeBlock, BlockData afterBlockData){
        beforeBlock.setBlockData(afterBlockData);
    }
}