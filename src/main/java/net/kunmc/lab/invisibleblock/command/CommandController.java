package net.kunmc.lab.invisibleblock.command;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import net.kunmc.lab.invisibleblock.packet.WrapperPlayServerMultiBlockChange;
import net.kunmc.lab.invisibleblock.util.DecolationConst;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                    CommandConst.COMMAND_CONFIG_CHANGE_TIME_SPAN)
                    .filter(e -> e.startsWith(args[0])).collect(Collectors.toList()));
        } else if (args.length == 2 && args[0].equals(CommandConst.COMMAND_CONFIG_CHANGE_TIME_SPAN)) {
            completions.add("<数字>");
        } else if (args.length == 2 && args[0].equals(CommandConst.COMMAND_ADD)) {
            List<String> tmpCompletions = new ArrayList<>();
            tmpCompletions.addAll(Arrays.asList("@a", "@p", "@r", "@s"));
            tmpCompletions.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
            completions.addAll(tmpCompletions.stream().filter(e -> e.startsWith(args[1])).collect(Collectors.toList()));
        }
        return completions;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(DecolationConst.RED + "引数がありません");
            return true;
        }
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = pm.createPacket(PacketType.Play.Server.MULTI_BLOCK_CHANGE);

        String commandName = args[0];
        WrapperPlayServerMultiBlockChange mbc = new WrapperPlayServerMultiBlockChange();
        mbc.getChunk();
        return true;
    }
}
