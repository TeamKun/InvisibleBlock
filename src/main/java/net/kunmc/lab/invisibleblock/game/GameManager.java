package net.kunmc.lab.invisibleblock.game;

import net.kunmc.lab.invisibleblock.block.InvisibleBlockData;

import java.util.*;

public class GameManager {
    public static GameMode runningMode = GameMode.MODE_NEUTRAL;
    // ブロックを透明にする対象プレイヤー
    public static Set<UUID> targetPlayer = new HashSet<>();

    // 透明対象となるブロックの全リスト、透明にした後再度ブロックを非透明するため保持する必要がある
    public static Map<String, InvisibleBlockData> targetBlock = new LinkedHashMap<>();
    // パケット送信対象となるBlockのリスト
    public static Map<String, InvisibleBlockData> sendTargetBlock = new LinkedHashMap<>();

    public static boolean isRevertBlock = false;

    public static void controller(GameMode runningMode) {
        // モードを設定
        GameManager.runningMode = runningMode;

        switch (runningMode) {
            case MODE_START:
                // 開始処理
                break;
            case MODE_NEUTRAL:
                isRevertBlock = false;
                // 停止処理
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