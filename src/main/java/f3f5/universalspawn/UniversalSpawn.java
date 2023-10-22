package f3f5.universalspawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class UniversalSpawn extends JavaPlugin implements Listener {

    private Location spawnLocation;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        loadSpawnLocation();

        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("Spawn has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Spawn has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.teleport(spawnLocation);
            } else {
                sender.sendMessage("Only players can use this command.");
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (player.hasPermission("universalspawn.setspawn")) {
                    Location currentLocation = player.getLocation();
                    saveSpawnLocation(currentLocation);
                    player.sendMessage("Spawn location has been set!");
                } else {
                    player.sendMessage("You don't have permission to use this command.");
                }
            } else {
                sender.sendMessage("Only players can use this command.");
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.teleport(spawnLocation);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        World configuredWorld = spawnLocation.getWorld();

        if (!(entity instanceof Player) || configuredWorld == null || !entity.getWorld().equals(configuredWorld) || event.getCause() != EntityDamageEvent.DamageCause.VOID) {
            return;
        }

        Player player = (Player) entity;
        player.teleport(spawnLocation);
        event.setCancelled(true);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Bukkit.getScheduler().runTaskLater(this, () -> player.spigot().respawn(), 1);
    }

    private void loadSpawnLocation() {
        FileConfiguration config = getConfig();
        World world = Bukkit.getWorld(config.getString("spawn.world"));
        double x = config.getDouble("spawn.x");
        double y = config.getDouble("spawn.y");
        double z = config.getDouble("spawn.z");
        float yaw = (float) config.getDouble("spawn.yaw");
        float pitch = (float) config.getDouble("spawn.pitch");

        spawnLocation = new Location(world, x, y, z, yaw, pitch);
    }

    private void saveSpawnLocation(Location location) {
        FileConfiguration config = getConfig();
        config.set("spawn.world", location.getWorld().getName());
        config.set("spawn.x", location.getX());
        config.set("spawn.y", location.getY());
        config.set("spawn.z", location.getZ());
        config.set("spawn.yaw", location.getYaw());
        config.set("spawn.pitch", location.getPitch());
        saveConfig();

        spawnLocation = location;
    }
}