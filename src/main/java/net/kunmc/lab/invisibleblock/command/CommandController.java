package net.kunmc.lab.invisibleblock.command;

import net.kunmc.lab.invisibleblock.Config;
import net.kunmc.lab.invisibleblock.game.GameManager;
import net.kunmc.lab.invisibleblock.util.DecolationConst;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandController implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(Stream.of(
                    CommandConst.COMMAND_START,
                    CommandConst.COMMAND_STOP,
                    CommandConst.COMMAND_ADD,
                    CommandConst.COMMAND_REMOVE,
                    CommandConst.COMMAND_CONFIG_TIME_SPAN,
                    CommandConst.COMMAND_CONFIG_MAX_REVERT_NUM,
                    CommandConst.COMMAND_SHOW_STATUS)
                    .filter(e -> e.startsWith(args[0])).collect(Collectors.toList()));
        } else if (args.length == 2 && (args[0].equals(CommandConst.COMMAND_CONFIG_TIME_SPAN) || args[0].equals(CommandConst.COMMAND_CONFIG_MAX_REVERT_NUM))) {
            completions.add("<数字>");
        } else if (args.length == 2 && args[0].equals(CommandConst.COMMAND_ADD)) {
            List<String> tmpCompletions = new ArrayList<>();
            tmpCompletions.addAll(Arrays.asList("@a", "@p", "@r", "@s"));
            tmpCompletions.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
            completions.addAll(tmpCompletions.stream().filter(e -> e.startsWith(args[1])).collect(Collectors.toList()));
        } else if (args.length == 2 && args[0].equals(CommandConst.COMMAND_REMOVE)) {
            List<String> tmpCompletions = new ArrayList<>();
            tmpCompletions.addAll(Arrays.asList("@a", "@p", "@r", "@s"));
            tmpCompletions.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
            completions.addAll(tmpCompletions.stream()
                    .filter(e -> (Bukkit.getPlayer(e) != null && GameManager.targetPlayer.contains(Bukkit.getPlayer(e).getUniqueId())) || e.startsWith("@"))
                    .filter(e -> e.startsWith(args[1])).collect(Collectors.toList()));
        }
        return completions;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendUsage(sender);
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage("このコマンドはゲーム内からのみ実行できます");
            return true;
        }

        String commandName = args[0];
        switch (commandName) {
            case CommandConst.COMMAND_START:
                if (GameManager.runningMode == GameManager.GameMode.MODE_START) {
                    sender.sendMessage(DecolationConst.RED + "すでに開始しています");
                    return true;
                } else if (GameManager.runningMode == GameManager.GameMode.MODE_STOPPING) {
                    sender.sendMessage(DecolationConst.AQUA + "可視化中はコマンドを受け付けていません");
                    return true;
                }
                GameManager.controller(GameManager.GameMode.MODE_START);
                sender.sendMessage(DecolationConst.GREEN + "ブロック透過を開始します");
                break;
            case CommandConst.COMMAND_STOP:
                if (GameManager.runningMode == GameManager.GameMode.MODE_NEUTRAL) {
                    sender.sendMessage(DecolationConst.RED + "開始されていません");
                    return true;
                } else if (GameManager.runningMode == GameManager.GameMode.MODE_STOPPING) {
                    sender.sendMessage(DecolationConst.AQUA + "可視化中はコマンドを受け付けていません");
                    return true;
                }
                GameManager.controller(GameManager.GameMode.MODE_STOPPING);
                sender.sendMessage(DecolationConst.GREEN + "ブロック透過終了、可視化します");
                break;
            case CommandConst.COMMAND_ADD:
                if (args.length != 2) {
                    sender.sendMessage(DecolationConst.RED + "引数の数が不正です");
                    return true;
                }
                List<Entity> players;
                try {
                    players = Bukkit.selectEntities(sender, args[1]);
                } catch (Exception e) {
                    sender.sendMessage(DecolationConst.RED + "存在しないプレイヤー名です");
                    return true;
                }
                if (players.isEmpty() || args[1].equals("@e")) {
                    sender.sendMessage(DecolationConst.RED + "存在しないまたはサーバに接続していないプレイヤー名です");
                    return true;
                }
                for (Entity p : players) {
                    if (GameManager.targetPlayer.contains(p.getUniqueId())) {
                        sender.sendMessage(DecolationConst.AQUA + p.getName() + "は追加済みです");
                    } else {
                        GameManager.targetPlayer.add(p.getUniqueId());
                        sender.sendMessage(DecolationConst.GREEN + p.getName() + "を追加しました");
                    }
                }
                break;
            case CommandConst.COMMAND_REMOVE:
                if (args.length != 2) {
                    sender.sendMessage(DecolationConst.RED + "引数の数が不正です");
                    return true;
                }
                try {
                    players = Bukkit.selectEntities(sender, args[1]);
                } catch (Exception e) {
                    sender.sendMessage(DecolationConst.RED + "存在しないプレイヤー名です");
                    return true;
                }
                if (players.isEmpty() || args[1].equals("@e")) {
                    sender.sendMessage(DecolationConst.RED + "存在しないまたはサーバに接続していないプレイヤー名です");
                    return true;
                }
                for (Entity p : players) {
                    if (!GameManager.targetPlayer.contains(p.getUniqueId())) {
                        sender.sendMessage(DecolationConst.AQUA + p.getName() + "は追加されていません");
                    } else {
                        GameManager.targetPlayer.remove(p.getUniqueId());
                        sender.sendMessage(DecolationConst.GREEN + p.getName() + "を削除しました");
                    }
                }
                break;
            case CommandConst.COMMAND_CONFIG_TIME_SPAN:
                if (args.length != 2) {
                    sender.sendMessage(DecolationConst.RED + "引数の数が不正です");
                    return true;
                }
                int ret = validateNum(sender, args[1]);
                if (ret != -1) {
                    Config.time = ret;
                    sender.sendMessage(DecolationConst.GREEN + "timeの値を" + Config.time + "に変更しました");
                }
                break;
            case CommandConst.COMMAND_CONFIG_MAX_REVERT_NUM:
                if (args.length != 2) {
                    sender.sendMessage(DecolationConst.RED + "引数の数が不正です");
                    return true;
                }
                ret = validateNum(sender, args[1]);
                if (ret != -1) {
                    Config.maxRevertNum = ret;
                    sender.sendMessage(DecolationConst.GREEN + "maxRevertNumの値を" + Config.maxRevertNum + "に変更しました");
                }
                break;
            case CommandConst.COMMAND_SHOW_STATUS:
                if (args.length != 1) {
                    sender.sendMessage(DecolationConst.RED + "引数の数が不正です");
                    return true;
                }
                sender.sendMessage(DecolationConst.GREEN + "設定値一覧");
                List<String> playerList = new ArrayList();
                for (UUID id : GameManager.targetPlayer) {
                    if (Bukkit.getPlayer(id) != null) {
                        playerList.add(Bukkit.getPlayer(id).getName());
                    }
                }
                sender.sendMessage("  addedPlayer" + playerList);
                sender.sendMessage("  time: " + Config.time);
                sender.sendMessage("  maxRevertNum: " + Config.maxRevertNum);
                String gameStatus = "スタート前";
                if (GameManager.runningMode == GameManager.GameMode.MODE_START) {
                    gameStatus = "透明化稼働中";
                } else if (GameManager.runningMode == GameManager.GameMode.MODE_STOPPING) {
                    gameStatus = "可視化中";
                }
                sender.sendMessage("  gameStatus: " + gameStatus);
                break;
            default:
                sender.sendMessage(DecolationConst.RED + "存在しないコマンドです");
        }
        return true;
    }

    private void sendUsage(CommandSender sender) {
        String usagePrefix = String.format("  /%s ", CommandConst.MAIN_COMMAND);
        String descPrefix = "  ";
        sender.sendMessage(DecolationConst.GREEN + "Usage:");
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, CommandConst.COMMAND_START));
        sender.sendMessage(String.format("%s透明化開始", descPrefix));
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, CommandConst.COMMAND_STOP));
        sender.sendMessage(String.format("%s透明化を終了して可視化を開始", descPrefix));
        sender.sendMessage(String.format("%s%s <Player名|@a等>"
                , usagePrefix, CommandConst.COMMAND_ADD));
        sender.sendMessage(String.format("%s置いたブロックを透明化させるユーザを追加", descPrefix));
        sender.sendMessage(String.format("%s%s <Player名|@a等>"
                , usagePrefix, CommandConst.COMMAND_REMOVE));
        sender.sendMessage(String.format("%saddコマンドで追加したユーザを削除", descPrefix));
        sender.sendMessage(String.format("%s%s <number>"
                , usagePrefix, CommandConst.COMMAND_CONFIG_TIME_SPAN));
        sender.sendMessage(String.format("%sブロックを透明化させるまでの秒数（numberは1以上の整数）", descPrefix));
        sender.sendMessage(String.format("%s%s <number>"
                , usagePrefix, CommandConst.COMMAND_CONFIG_MAX_REVERT_NUM));
        sender.sendMessage(String.format("%s1Tickあたりに透明化を解除するブロックの個数（numberは1以上の整数）", descPrefix));
        sender.sendMessage(String.format("%s%s"
                , usagePrefix, CommandConst.COMMAND_SHOW_STATUS));
        sender.sendMessage(String.format("%s設定などゲームの状態を確認", descPrefix));

    }

    private int validateNum(CommandSender sender, String target) {
        // 不正な場合は-1を返す
        int num;
        try {
            num = Integer.parseInt(target);
        } catch (NumberFormatException e) {
            sender.sendMessage(DecolationConst.RED + "整数以外が入力されています");
            return -1;
        }
        if (num < 1) {
            sender.sendMessage(DecolationConst.RED + "0より大きい整数を入力してください");
            return -1;
        }
        return num;
    }
}
