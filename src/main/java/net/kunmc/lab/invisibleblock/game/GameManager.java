package net.kunmc.lab.invisibleblock.game;

import net.kunmc.lab.invisibleblock.block.InvisibleBlockData;

import java.util.*;

public class GameManager {
    public static GameMode runningMode = GameMode.MODE_NEUTRAL;
    // ブロックを透明にする対象プレイヤー
    public static List<UUID> targetPlayer = new ArrayList<>();

    // 透明対象となるブロックの全リスト、透明にした後再度ブロックを非透明するため保持する必要がある
    public static List<InvisibleBlockData> targetBlock = new ArrayList<>();
    // パケット送信対象となるBlockのリスト
    public static List<InvisibleBlockData> sendTargetBlock = new ArrayList<>();

    public static void controller(GameMode runningMode) {
        // モードを設定
        GameManager.runningMode = runningMode;

        switch (runningMode) {
            case MODE_START:
                // 開始処理
                break;
            case MODE_NEUTRAL:
                // 停止処理
                break;
        }
    }

    public enum GameMode {
        // ゲーム外の状態
        MODE_NEUTRAL,
        MODE_START
    }
}