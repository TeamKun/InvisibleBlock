package net.kunmc.lab.invisibleblock.block;

import net.kunmc.lab.invisibleblock.Config;
import net.kunmc.lab.invisibleblock.game.GameManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.Bed;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.data.type.TrapDoor;

import java.util.ArrayList;
import java.util.List;

public class BlockConvert {
    public static void changeToBarrier(InvisibleBlockData ib) {
        /**
         * 以下のブロックは単純にコピーできないため、専用のMapを用意して必要な情報を保持しておく
         * - トラップドア
         *   - トラップドアのon/off、ドアなどと異なり、特定の状態で建築に使われることがあるので持っておく
         * */

        // ドア・トラップドア対応
        if (ib.blockData instanceof TrapDoor) {
            GameManager.trapDoorBlock.put(BlockConvert.createLocationKeyFromBlock(ib.block), ((Openable) ib.block.getBlockData()).isOpen());
        } else if (ib.blockData instanceof Door && ((Door) ib.blockData).getHalf() == Bisected.Half.BOTTOM) {
            GameManager.DoorBlock.put(BlockConvert.createLocationKeyFromBlock(ib.block), ((Openable) ib.block.getBlockData()).isOpen());
        }

        // 透明化
        ib.block.setType(Material.BARRIER);
    }

    public static void revertBlock() {
        // 1Tickあたり2000ブロックまでの処理に留めておく
        List<String> reverted = new ArrayList<>();
        int cnt = 0;
        for (InvisibleBlockData ib : GameManager.targetBlock.values()) {
            String key = BlockConvert.createLocationKeyFromBlock(ib.block);
            ib.block.setBlockData(ib.blockData);
            if (GameManager.targetBlock.containsKey(key) && GameManager.signBlock.containsKey(key)) {
                List<String> lines = GameManager.signBlock.get(key);
                Sign sign = (Sign) ib.block.getState();
                for (int i = 0; i < lines.size(); i++) {
                    sign.setLine(i, lines.get(i));
                }
                sign.update();
            } else if (GameManager.targetBlock.containsKey(key) && GameManager.trapDoorBlock.containsKey(key) && ib.blockData instanceof TrapDoor) {
                BlockData data = ib.block.getBlockData();
                Openable door = ((Openable) data);
                door.setOpen(GameManager.trapDoorBlock.get(key));
                ib.block.setBlockData(data);
            }
            reverted.add(key);
            cnt++;
            // RevertのMaxを超えたら次に回す、ただしベッドやドアは跨ぐと面倒なことになるので跨がないようにする
            if (cnt >= Config.maxRevertNum && !(ib.blockData instanceof Bed) && !(ib.blockData instanceof Door)) break;
        }
        afterConvert(reverted);
        for (String key : reverted) {
            GameManager.targetBlock.remove(key);
        }
    }

    public static void afterConvert(List<String> keys) {
        //ブロックの可視化してから実施した方が都合が良い処理を行う

        // ドアの開閉設定
        for (String key : keys) {
            if (!GameManager.DoorBlock.containsKey(key)) continue;

            Block b = GameManager.targetBlock.get(key).block;
            BlockData data = b.getBlockData();
            Openable door = ((Openable) data);
            door.setOpen(GameManager.DoorBlock.get(key));
            b.setBlockData(data);
        }
    }

    public static String createLocationKeyFromBlock(Block block) {
        Location loc = block.getLocation();
        return loc.getX() + " " + loc.getY() + " " + loc.getZ();
    }
}
