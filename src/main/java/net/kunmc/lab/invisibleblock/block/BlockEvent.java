package net.kunmc.lab.invisibleblock.block;

import net.kunmc.lab.invisibleblock.game.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockMultiPlaceEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.ArrayList;
import java.util.List;

public class BlockEvent implements Listener {

    @EventHandler
    public void onBucketEmptyPlace(PlayerBucketEmptyEvent e) {
        if (GameManager.runningMode != GameManager.GameMode.MODE_START)
            return;

        if (!GameManager.targetPlayer.contains(e.getPlayer().getUniqueId()))
            return;

        BlockData blockData;
        if (e.getBucket().equals(Material.LAVA_BUCKET)) {
            blockData = Material.LAVA.createBlockData();
        } else {
            blockData = Material.WATER.createBlockData();
        }

        GameManager.sendTargetBlock.put(BlockConvert.createLocationKeyFromBlock(e.getBlock()), new InvisibleBlockData(e.getBlock(), blockData));
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (GameManager.runningMode != GameManager.GameMode.MODE_START)
            return;

        if (!GameManager.targetPlayer.contains(e.getPlayer().getUniqueId()))
            return;

        Block b = e.getBlock();
        BlockData bd = e.getBlock().getBlockData();
        if (bd instanceof Bed || bd instanceof Door) return;

        String key = BlockConvert.createLocationKeyFromBlock(b);
        GameManager.sendTargetBlock.put(key, new InvisibleBlockData(b, bd.clone()));
    }

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        Block b = e.getBlock();

        List<String> lines = new ArrayList<>();
        for (int i = 0; i < e.getLines().length; i++) {
            System.out.println(e.getLine(i));
            lines.add(e.getLine(i));
        }
        GameManager.signBlock.put(BlockConvert.createLocationKeyFromBlock(b), lines);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (GameManager.runningMode != GameManager.GameMode.MODE_START)
            return;

        Block b = e.getBlock();

        String key = BlockConvert.createLocationKeyFromBlock(b);
        if (GameManager.sendTargetBlock.containsKey(key)) GameManager.sendTargetBlock.remove(key);
        if (GameManager.targetBlock.containsKey(key)) GameManager.targetBlock.remove(key);
        if (GameManager.signBlock.containsKey(key)) GameManager.signBlock.remove(key);
        if (GameManager.trapDoorBlock.containsKey(key)) GameManager.trapDoorBlock.remove(key);
    }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {
        if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL)
            return;

        Material type = event.getEntity().getItemStack().getType();
        if (isBed(type) || isDoor(type)) {
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void onBlockMultiPlace(BlockMultiPlaceEvent e) {
        if (GameManager.runningMode != GameManager.GameMode.MODE_START)
            return;

        if (!GameManager.targetPlayer.contains(e.getPlayer().getUniqueId()))
            return;

        Block b = e.getBlock();
        BlockData bd = e.getBlock().getBlockData();
        Location loc = e.getBlock().getLocation();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();
        World w = e.getPlayer().getWorld();
        GameManager.sendTargetBlock.put(BlockConvert.createLocationKeyFromBlock(b), new InvisibleBlockData(b, bd.clone()));

        if (bd instanceof Bed) {
            int[][] point = {{x + 1, z}, {x, z + 1}, {x - 1, z}, {x, z - 1}};
            for (int[] p : point) {
                Block checkBlock = w.getBlockAt(p[0], y, p[1]);
                if (isTargetBed(b, checkBlock)) {
                    GameManager.sendTargetBlock.put(BlockConvert.createLocationKeyFromBlock(checkBlock), new InvisibleBlockData(checkBlock, checkBlock.getBlockData().clone()));
                }
            }
        } else if (bd instanceof Door) {
            int[] point = {y - 1, y + 1};
            for (int p : point) {
                Block checkBlock = w.getBlockAt(x, p, z);
                if (isTargetDoor(b, checkBlock)) {
                    GameManager.sendTargetBlock.put(BlockConvert.createLocationKeyFromBlock(checkBlock), new InvisibleBlockData(checkBlock, checkBlock.getBlockData().clone()));
                }
            }
        }
    }

    private boolean isTargetBed(Block b, Block checkBlock) {
        if (!(checkBlock.getBlockData() instanceof Bed)) return false;

        Bed.Part checkPart = Bed.Part.HEAD;
        if (((Bed) b.getBlockData()).getPart() == Bed.Part.HEAD) {
            checkPart = Bed.Part.FOOT;
        }

        if (((Bed) checkBlock.getBlockData()).getPart() == checkPart && ((Bed) b.getBlockData()).getFacing() == ((Bed) checkBlock.getBlockData()).getFacing()) {
            return true;
        }

        return false;
    }

    private boolean isTargetDoor(Block b, Block checkBlock) {
        if (!(checkBlock.getBlockData() instanceof Door)) return false;

        Bisected.Half checkPart = Bisected.Half.TOP;
        if (((Door) b.getBlockData()).getHalf() == Bisected.Half.TOP) {
            checkPart = Bisected.Half.BOTTOM;
        }

        if (((Door) checkBlock.getBlockData()).getHalf() == checkPart) {
            return true;
        }

        return false;
    }

    private boolean isBed(Material material) {
        return material == Material.BLACK_BED ||
                material == Material.BLUE_BED ||
                material == Material.BROWN_BED ||
                material == Material.CYAN_BED ||
                material == Material.GRAY_BED ||
                material == Material.GREEN_BED ||
                material == Material.LIGHT_BLUE_BED ||
                material == Material.LIGHT_GRAY_BED ||
                material == Material.LIME_BED ||
                material == Material.MAGENTA_BED ||
                material == Material.ORANGE_BED ||
                material == Material.PINK_BED ||
                material == Material.PURPLE_BED ||
                material == Material.RED_BED ||
                material == Material.WHITE_BED ||
                material == Material.YELLOW_BED;
    }

    private boolean isDoor(Material material) {
        return material == Material.DARK_OAK_DOOR ||
                material == Material.ACACIA_DOOR ||
                material == Material.BIRCH_DOOR ||
                material == Material.IRON_DOOR ||
                material == Material.CRIMSON_DOOR ||
                material == Material.JUNGLE_DOOR ||
                material == Material.OAK_DOOR ||
                material == Material.SPRUCE_DOOR ||
                material == Material.WARPED_DOOR;
    }
}