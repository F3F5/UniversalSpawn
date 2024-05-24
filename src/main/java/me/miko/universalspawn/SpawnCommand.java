package me.miko.universalspawn;

import com.tcoded.folialib.FoliaLib;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private final UniversalSpawn plugin;
    final FoliaLib foliaLib = UniversalSpawn.getFoliaLib();

    public SpawnCommand(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Only players can use this command.");
                return true;
            }

            Player player = (Player) sender;

            if (args.length > 0 && args[0].equalsIgnoreCase("set")) {
                if (!player.hasPermission("universalspawn.spawn.set")) {
                    player.sendMessage("You don't have permission to use this command.");
                    return true;
                }

                plugin.setSpawnLocation(player.getLocation());
                plugin.getConfigManager().saveSpawnLocation(player.getLocation());
                player.sendMessage("Spawn location has been set!");
            } else {
                foliaLib.getImpl().teleportAsync(player, plugin.getSpawnLocation());
            }
            return true;
        }
        return false;
    }
}
