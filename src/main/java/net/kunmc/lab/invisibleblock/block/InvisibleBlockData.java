package net.kunmc.lab.invisibleblock.block;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class InvisibleBlockData {
    public int tick = 0;

    // 対象Block
    public Block block;
    //  対象Blockの元々のblockData
    public BlockData blockData;

    public InvisibleBlockData(Block block, BlockData blockData) {
        this.block = block;
        this.blockData = blockData;
    }

    public void incrementTick() {
        tick++;
    }
}
