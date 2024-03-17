package me.miko.universalspawn;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCommand implements CommandExecutor {
    private UniversalSpawn plugin;

    public SpawnCommand(UniversalSpawn plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("set")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("universalspawn.spawn-set")) {
                        plugin.setSpawnLocation(player.getLocation());
                        plugin.getConfigManager().saveSpawnLocation(player.getLocation());
                        player.sendMessage("Spawn location has been set!");
                    } else {
                        player.sendMessage("You don't have permission to use this command.");
                    }
                } else {
                    sender.sendMessage("Only players can use this command.");
                }
                return true;
            } else {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.teleport(plugin.getSpawnLocation());
                } else {
                    sender.sendMessage("Only players can use this command.");
                }
                return true;
            }
        }
        return false;
    }
}