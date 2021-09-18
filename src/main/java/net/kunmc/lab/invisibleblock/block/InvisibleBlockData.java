package net.kunmc.lab.invisibleblock.block;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.ChunkCoordIntPair;
import net.kunmc.lab.invisibleblock.packet.BlockChangeArray;
import net.kunmc.lab.invisibleblock.packet.WrapperPlayServerMultiBlockChange;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

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

    public void incrementTick (){
        tick++;
    }
}
