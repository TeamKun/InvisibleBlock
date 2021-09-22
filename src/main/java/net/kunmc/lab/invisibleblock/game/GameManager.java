package net.kunmc.lab.invisibleblock.game;

import net.kunmc.lab.invisibleblock.block.BlockConvert;
import net.kunmc.lab.invisibleblock.block.InvisibleBlockData;
import org.bukkit.World;

import java.util.*;

public class GameManager {
    public static GameMode runningMode = GameMode.MODE_NEUTRAL;
    // ブロックを透明にする対象プレイヤー
    public static Set<UUID> targetPlayer = new HashSet<>();

    // 透明対象となるブロックの全リスト、透明にした後再度ブロックを非透明するため保持する必要がある
    public static Map<String, InvisibleBlockData> targetBlock = new LinkedHashMap<>();
    // パケット送信対象となるBlockのリスト
    public static Map<String, InvisibleBlockData> sendTargetBlock = new LinkedHashMap<>();

    // 看板文字を保持する
    public static Map<String, List<String>> signBlock = new HashMap<>();
    // トラップドアの開閉を保持する
    public static Map<String, Boolean> trapDoorBlock = new HashMap<>();
    // ドアの開閉を保持する
    public static Map<String, Boolean> DoorBlock = new HashMap<>();

    public static boolean isRevertBlock = false;

    public static void controller(GameMode runningMode) {
        // モードを設定
        GameManager.runningMode = runningMode;

        switch (runningMode) {
            case MODE_START:
                break;
            case MODE_NEUTRAL:
                isRevertBlock = false;
                trapDoorBlock.clear();
                DoorBlock.clear();
                signBlock.clear();
                break;
            case MODE_STOPPING:
                isRevertBlock = true;
                sendTargetBlock.clear();
               // 透明化停止 ~ 可視化する状態時のモーション
                break;
        }
    }

    public enum GameMode {
        // ゲーム外の状態
        MODE_NEUTRAL,
        MODE_START,
        MODE_STOPPING
    }
}