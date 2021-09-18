package net.kunmc.lab.invisibleblock.block;

import org.bukkit.block.Block;

public class InvisibleBlockData {
    private int tick = 0;
    Block block;
    public boolean invisibled = false;
    public InvisibleBlockData(Block block) {
        this.block = block;
    }

    public void incrementTick (){
        tick++;
    }

    public void sendInvisiblePacket (){
    }
}
