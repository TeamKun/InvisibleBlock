package net.kunmc.lab.invisibleblock;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.kunmc.lab.invisibleblock.command.CommandConst;
import net.kunmc.lab.invisibleblock.command.CommandController;
import net.kunmc.lab.invisibleblock.block.BlockEvent;
//import net.kunmc.lab.invisibleblock.packet.PacketListener;
import net.kunmc.lab.invisibleblock.task.Task;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class InvisibleBlock extends JavaPlugin {
    private static InvisibleBlock plugin;
    private BukkitTask task;
    private ProtocolManager protocolManager;

    public static InvisibleBlock getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        Config.loadConfig();
        getCommand(CommandConst.MAIN_COMMAND).setExecutor(new CommandController());
        getServer().getPluginManager().registerEvents(new BlockEvent(), plugin);
        //protocolManager = ProtocolLibrary.getProtocolManager();
        //protocolManager.addPacketListener(new PacketListener(plugin, PacketType.Play.Server.BLOCK_CHANGE));

        task = new Task(plugin).runTaskTimer(this, 0, 1);
        getLogger().info("InvisibleBlock Plugin is enabled");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
